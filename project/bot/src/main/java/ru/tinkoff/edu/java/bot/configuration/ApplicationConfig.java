package ru.tinkoff.edu.java.bot.configuration;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;

import java.net.URI;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@ComponentScan
public record ApplicationConfig( @NonNull String test, @NonNull String telegramToken, @NonNull URI scrapperBaseUrl )
{
	@Bean
	public @NonNull ScrapperClient scrapperClient()
	{
		return new ScrapperClient( scrapperBaseUrl );
	}
}
