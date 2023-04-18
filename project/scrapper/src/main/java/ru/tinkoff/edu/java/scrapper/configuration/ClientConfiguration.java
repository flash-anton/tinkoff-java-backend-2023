package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.tinkoff.edu.java.scrapper.botclient.BotClient;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

import java.util.Map;
import java.util.Objects;

@Validated
@ConfigurationProperties( prefix = "clients" )
public record ClientConfiguration( @NonNull WebClientDefaultConfig github, @NonNull WebClientDefaultConfig stackoverflow, @NonNull WebClientDefaultConfig bot )
{
	@Bean
	public GitHubClient gitHubClient()
	{
		return new GitHubClient( github.toWebClient( null ) );
	}

	@Bean
	public StackOverflowClient stackOverflowClient()
	{
		return new StackOverflowClient( stackoverflow.toWebClient( null ) );
	}

	@Bean
	public BotClient botClient()
	{
		return new BotClient( bot.defaultBaseUrl );
	}

	private record WebClientDefaultConfig( @NonNull String defaultBaseUrl, Map<String, String> defaultHeaders, Boolean compress )
	{
		public WebClient toWebClient( String baseUrl )
		{
			WebClient.Builder builder = WebClient.builder();

			builder.baseUrl( Objects.requireNonNullElse( baseUrl, defaultBaseUrl ) );

			if( defaultHeaders != null )
			{
				defaultHeaders.forEach( builder::defaultHeader );
			}

			if( Objects.requireNonNullElse( compress, false ) )
			{
				HttpClient httpClient = HttpClient.create().compress( true );
				builder.clientConnector( new ReactorClientHttpConnector( httpClient ) );
			}

			builder.exchangeStrategies( ExchangeStrategies
				.builder()
				.codecs( codecs -> codecs
					.defaultCodecs()
					.maxInMemorySize( 10_000_000 ) )
				.build()
			);

			return builder.build();
		}
	}
}
