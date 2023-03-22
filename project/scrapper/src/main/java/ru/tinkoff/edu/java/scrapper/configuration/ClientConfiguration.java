package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;

@Validated
@ConfigurationProperties( prefix = "client", ignoreUnknownFields = false )
@Data
public final class ClientConfiguration
{
	private final String gitHubApiBaseUrl;

	@Bean
	public GitHubClient gitHubClient()
	{
		if( gitHubApiBaseUrl == null || gitHubApiBaseUrl.isEmpty() )
		{
			return new GitHubClient();
		}
		return new GitHubClient( gitHubApiBaseUrl );
	}
}
