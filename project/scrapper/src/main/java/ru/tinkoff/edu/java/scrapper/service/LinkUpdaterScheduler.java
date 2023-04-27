package ru.tinkoff.edu.java.scrapper.service;

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
import ru.tinkoff.edu.java.scrapper.dto.LinkChanges;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private class R
	{
		URI url;
		OffsetDateTime updated;
		LinkChanges changes = new LinkChanges( new ArrayList<>() );

		R( Link link )
		{
			url = link.url();
			updated = link.updated();
		}

		boolean fillChanges()
		{
			try
			{
				LinkContent linkContent = linkParser.parse( url );

				if( linkContent instanceof LinkContent.StackOverflowLinkContent so )
				{
					changes = stackOverflowClient.fetchQuestionInfo( so.questionId(), updated ).linkChanges();
				}
				else if( linkContent instanceof LinkContent.GitHubLinkContent gh )
				{
					changes = gitHubClient.fetchRepositoryInfo( gh.user(), gh.repository(), updated ).linkChanges();
				}
				else
				{
					logger.error( "Unsupported URL {}", url );
				}
			}
			catch( RuntimeException ex )
			{
				logger.error( ex );
			}
			return !changes.events().isEmpty();
		}

		void notifyChats()
		{
			try
			{
				long[] chatIds = linkService.getChats( url ).parallelStream().mapToLong( l -> l ).toArray();
				botClient.linkUpdate( changes.toString(), url, chatIds );
			}
			catch( RuntimeException ex )
			{
				logger.error( ex );
			}
		}
	}

	@Scheduled( fixedDelayString = "#{@schedulerIntervalMs}" )
	public void update()
	{
		List<R> changes = linkService
			.getLinks( OffsetDateTime.now() )
			.parallelStream()
			.map( R::new )
			.filter( R::fillChanges )
			.toList();

		try
		{
			Map<URI, OffsetDateTime> updates = changes
				.parallelStream()
				.collect( Collectors.toMap(
					r -> r.url,
					r -> r.changes.last().time() ) );

			linkService.update( updates );
		}
		catch( RuntimeException ex )
		{
			logger.error( ex );
			return;
		}
		finally
		{
			printDb();
		}

		changes
			.parallelStream()
			.forEach( R::notifyChats );
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
}