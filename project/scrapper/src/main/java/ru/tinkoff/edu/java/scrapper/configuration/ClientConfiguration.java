package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

@Validated
@ConfigurationProperties( prefix = "client", ignoreUnknownFields = false )
public record ClientConfiguration( String gitHubApiBaseUrl, String stackOverflowApiBaseUrl )
{
	@Bean
	public GitHubClient gitHubClient()
	{
		return new GitHubClient( gitHubApiBaseUrl );
	}

	@Bean
	public StackOverflowClient stackOverflowClient()
	{
		return new StackOverflowClient( stackOverflowApiBaseUrl );
	}
}
