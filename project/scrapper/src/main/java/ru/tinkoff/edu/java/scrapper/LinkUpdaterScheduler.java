package ru.tinkoff.edu.java.scrapper;

import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkContent;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.scrapper.botclient.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkChanges;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@ConfigurationPropertiesScan
@EnableScheduling
public class LinkUpdaterScheduler
{
	private final JdbcTemplate jdbcTemplate; // для отладки
	private final LinkService linkService;
	private final LinkParser linkParser;
	private final StackOverflowClient stackOverflowClient;
	private final GitHubClient gitHubClient;
	private final BotClient botClient;
	private final Logger logger = LogManager.getLogger();

	public LinkUpdaterScheduler(
		@NonNull JdbcTemplate jdbcTemplate,
		@NonNull @Qualifier( "JooqLinkService" ) LinkService linkService,
		@NonNull LinkParser linkParser,
		@NonNull StackOverflowClient stackOverflowClient,
		@NonNull GitHubClient gitHubClient,
		@NonNull BotClient botClient )
	{
		this.jdbcTemplate = jdbcTemplate;
		this.linkService = linkService;
		this.linkParser = linkParser;
		this.stackOverflowClient = stackOverflowClient;
		this.gitHubClient = gitHubClient;
		this.botClient = botClient;
	}

	@Scheduled( fixedDelayString = "#{@schedulerIntervalMs}" )
	public void update()
	{
		printDb();

		linkService
			.getLinks( OffsetDateTime.now() )
			.parallelStream()
			.forEach( link ->
			{
				try
				{
					processLink( link );
				}
				catch( RuntimeException ex )
				{
					logger.error( ex );
				}
			} );
	}

	private void printDb()
	{
		List<String> rows = jdbcTemplate.query( """
				select * from chat c
				full join chat_link cl on c.id = cl.chat_id
				full join link l on cl.link_url = l.url
				order by c.id
				""",
			( rs, rowNum ) -> String.join( " | ",
				String.valueOf( rs.getLong( "id" ) ),
				String.valueOf( rs.getTimestamp( "updated" ) ),
				rs.getString( "url" )
			)
		);

		logger.info( "\n{}", String.join( "\n", rows ) );
	}

	private void processLink( @NonNull Link link )
	{
		String url = link.url();
		LinkContent linkContent = linkParser.parse( URI.create( url ) );
		OffsetDateTime lastUpdated = link.updated();

		LinkChanges linkChanges;
		if( linkContent instanceof LinkContent.StackOverflowLinkContent so )
		{
			linkChanges = stackOverflowClient.fetchQuestionInfo( so.questionId(), lastUpdated ).linkChanges();
		}
		else if( linkContent instanceof LinkContent.GitHubLinkContent gh )
		{
			linkChanges = gitHubClient.fetchRepositoryInfo( gh.user(), gh.repository(), lastUpdated ).linkChanges();
		}
		else
		{
			logger.error( "Unsupported URL {}", url );
			return;
		}

		if( !linkChanges.events().isEmpty() )
		{
			long[] chatIds = linkService.getChats( url ).parallelStream().mapToLong( l -> l ).toArray();
			linkService.update( url, linkChanges.last().time() );
			botClient.linkUpdate( linkChanges.toString(), url, chatIds );
		}
	}
}