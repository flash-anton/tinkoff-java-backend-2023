package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryInfoResponse;

public record GitHubClient( @NonNull WebClient webClient )
{
	public GitHubRepositoryInfoResponse fetchRepositoryInfo( String user, String repository )
	{
		return webClient
			.get()
			.uri( "/repos/{user}/{repo}", user, repository )
			.retrieve()
			.bodyToMono( GitHubRepositoryInfoResponse.class )
			.block();
	}
}
