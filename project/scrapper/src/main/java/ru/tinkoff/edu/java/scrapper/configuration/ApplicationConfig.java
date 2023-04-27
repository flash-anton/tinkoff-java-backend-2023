package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.linkparser.GitHubLinkParser;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.linkparser.StackOverflowLinkParser;
import ru.tinkoff.edu.java.scrapper.botclient.BotClient;
import ru.tinkoff.edu.java.scrapper.botclient.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.dto.Scheduler;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.BotNotifier;
import ru.tinkoff.edu.java.scrapper.service.ScrapperQueueProducer;

import java.net.URI;

@Validated
@ConfigurationProperties( prefix = "app", ignoreUnknownFields = false )
public record ApplicationConfig(
	@NotNull String test,
	@NotNull Scheduler scheduler,
	@NotNull AccessType databaseAccessType,
	boolean useQueue )
{
	public enum AccessType
	{
		JDBC, JOOQ, JPA
	}

	@Bean
	public @NonNull BotNotifier botNotifier( @NonNull BotClient botClient, @NonNull ScrapperQueueProducer scrapperQueueProducer )
	{
		if( useQueue )
		{
			return ( String description, URI url, long[] tgChatIds ) ->
			{
				LinkUpdateRequest req = new LinkUpdateRequest();
				req.setUrl( url );
				req.setDescription( description );
				req.setTgChatIds( tgChatIds );
				scrapperQueueProducer.send( req );
			};
		}
		else
		{
			return botClient::linkUpdate;
		}
	}

	@Configuration
	@ConditionalOnProperty( prefix = "app", name = "database-access-type", havingValue = "jdbc" )
	public static class JdbcAccessConfiguration
	{
		@Bean
		public ChatLinkRepository chatLinkRepository( JdbcTemplate jdbcTemplate )
		{
			return new JdbcChatLinkRepository( jdbcTemplate );
		}

		@Bean
		public ChatRepository chatRepository( JdbcTemplate jdbcTemplate )
		{
			return new JdbcChatRepository( jdbcTemplate );
		}

		@Bean
		public LinkRepository linkRepository( JdbcTemplate jdbcTemplate )
		{
			return new JdbcLinkRepository( jdbcTemplate );
		}
	}

	@Configuration
	@ConditionalOnProperty( prefix = "app", name = "database-access-type", havingValue = "jooq" )
	public static class JooqAccessConfiguration
	{
		@Bean
		public ChatLinkRepository chatLinkRepository( DSLContext dsl )
		{
			return new JooqChatLinkRepository( dsl );
		}

		@Bean
		public ChatRepository chatRepository( DSLContext dsl )
		{
			return new JooqChatRepository( dsl );
		}

		@Bean
		public LinkRepository linkRepository( DSLContext dsl )
		{
			return new JooqLinkRepository( dsl );
		}
	}

	@Configuration
	@ConditionalOnProperty( prefix = "app", name = "database-access-type", havingValue = "jpa" )
	public static class JpaAccessConfiguration
	{
		@Bean
		public ChatLinkRepository chatLinkRepository( EntityManager entityManager )
		{
			return new JpaChatLinkRepository( entityManager );
		}

		@Bean
		public ChatRepository chatRepository( EntityManager entityManager )
		{
			return new JpaChatRepository( entityManager );
		}

		@Bean
		public LinkRepository linkRepository( EntityManager entityManager )
		{
			return new JpaLinkRepository( entityManager );
		}
	}

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
