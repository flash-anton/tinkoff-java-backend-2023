package ru.tinkoff.edu.java.scrapper.webclient;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.GitHubRepositoryInfoResponse;

public class GitHubClient
{
	protected final WebClient client;

	public GitHubClient()
	{
		this( null );
	}

	public GitHubClient( String baseUrl )
	{
		client = WebClientBuilder.create( baseUrl, "https://api.github.com" )
								 .defaultHeader( "Accept", "application/vnd.github+json" )
								 .defaultHeader( "X-GitHub-Api-Version", "2022-11-28" )
								 .build();
	}

	public GitHubRepositoryInfoResponse fetchRepositoryInfo( String user, String repository )
	{
		return client.get()
					 .uri( "/repos/{user}/{repo}", user, repository )
					 .retrieve()
					 .bodyToMono( GitHubRepositoryInfoResponse.class )
					 .block();
	}
}
