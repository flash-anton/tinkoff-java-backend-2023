package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryInfoResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkChanges;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public record GitHubClient( @NonNull WebClient webClient )
{
	public @NonNull GitHubRepositoryInfoResponse fetchRepositoryInfo( @NonNull String user, @NonNull String repository, @NonNull OffsetDateTime lastUpdated )
	{
		record GitHubEvent( @NonNull OffsetDateTime created_at, @NonNull String type ) {}

		List<GitHubEvent> response = webClient
			.get()
			.uri( "/repos/{user}/{repo}/events", user, repository )
			.retrieve()
			.bodyToFlux( GitHubEvent.class )
			.collectList()
			.block();

		if( response == null )
		{
			response = new ArrayList<>();
		}

		LinkChanges linkChanges = new LinkChanges( response
			.parallelStream()
			.filter( e -> e.created_at.isAfter( lastUpdated ) )
			.map( e -> new LinkChanges.Event( e.created_at, e.type ) )
			.distinct()
			.toList() );

		return new GitHubRepositoryInfoResponse( user, repository, linkChanges );
	}
}
