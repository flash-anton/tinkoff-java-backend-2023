package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.linkparser.GitHubLinkParser;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.linkparser.StackOverflowLinkParser;
import ru.tinkoff.edu.java.scrapper.dto.Scheduler;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig( @NotNull String test, @NotNull Scheduler scheduler )
{
	@Bean
	public long schedulerIntervalMs()
	{
		return scheduler.interval().toMillis();
	}

	@Bean
	public @NotNull LinkParser linkParser()
	{
		LinkParser linkParser = new StackOverflowLinkParser();
		linkParser.setNext( new GitHubLinkParser() );
		return linkParser;
	}
}
