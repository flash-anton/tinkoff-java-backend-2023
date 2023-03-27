package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.tg.TgBot;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig( @NotNull String test, @NotNull String telegramToken )
{
	private static final ConcurrentMap<Long, Set<String> > users = new ConcurrentHashMap<>();

	@Bean
	public TgBot getTgBot( ApplicationConfig config )
	{
		return new TgBot( config );
	}

	public ConcurrentMap<Long, Set<String> > getTgUsers()
	{
		return users;
	}
}
