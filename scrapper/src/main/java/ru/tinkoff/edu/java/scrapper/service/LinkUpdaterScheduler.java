package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkContent;
import ru.tinkoff.edu.java.linkparser.LinkParser;
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
	private final LinkService linkService;
	private final LinkParser linkParser;
	private final StackOverflowClient stackOverflowClient;
	private final GitHubClient gitHubClient;
	private final BotNotifier botNotifier;

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
				botNotifier.linkUpdate( changes.toString(), url, chatIds );
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

		changes
			.parallelStream()
			.forEach( R::notifyChats );
	}
}