package ru.tinkoff.edu.java.scrapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkContent;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.scrapper.botclient.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryInfoResponse;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
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

	@Scheduled( fixedDelayString = "#{@schedulerIntervalMs}" )
	public void update()
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

	private void processLink( @NonNull Link link )
	{
		String url = link.url();
		OffsetDateTime updatedOld = link.updated();
		long[] chatIds = linkService.getChats( url ).parallelStream().mapToLong( l -> l ).toArray();
		LinkContent linkContent = linkParser.parse( URI.create( url ) );

		OffsetDateTime updated;
		if( linkContent instanceof LinkContent.StackOverflowLinkContent so )
		{
			updated = stackOverflowClient.fetchQuestionInfo( so.questionId() ).last_activity_date();
		}
		else if( linkContent instanceof LinkContent.GitHubLinkContent gh )
		{
			GitHubRepositoryInfoResponse info = gitHubClient.fetchRepositoryInfo( gh.user(), gh.repository() );
			updated = info.updated_at().isAfter( info.pushed_at() ) ? info.updated_at() : info.pushed_at();
		}
		else
		{
			logger.error( "Unsupported URL {}", url );
			return;
		}

		if( updated.isAfter( updatedOld ) )
		{
			linkService.update( url, updated );
			botClient.linkUpdate( "Ресурс обновлен " + updated, url, chatIds );
		}
	}
}