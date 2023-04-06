package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@ComponentScan
public record ApplicationConfig( @NotNull String test, @NotNull String telegramToken )
{
	private static final ConcurrentMap<Long, Set<String> > users = new ConcurrentHashMap<>();

	public ConcurrentMap<Long, Set<String> > getTgUsers()
	{
		return users;
	}
}
