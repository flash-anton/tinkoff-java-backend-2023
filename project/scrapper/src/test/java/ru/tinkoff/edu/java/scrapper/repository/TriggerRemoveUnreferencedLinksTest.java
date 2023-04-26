package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TriggerRemoveUnreferencedLinksTest extends IntegrationEnvironment
{
	private final long CHAT_WITH_COMMON_URL = 1;
	private final long CHAT_WITH_BOTH_URLS = 2;
	private final String COMMON_URL = "url-common";
	private final String SINGLE_URL = "url-single";

	@BeforeEach
	void beforeEach()
	{
		jdbcTemplate.update( JdbcChatRepository.SQL_INSERT, CHAT_WITH_COMMON_URL );
		jdbcTemplate.update( JdbcChatRepository.SQL_INSERT, CHAT_WITH_BOTH_URLS );

		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, COMMON_URL );
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, SINGLE_URL );

		jdbcTemplate.update( JdbcChatLinkRepository.SQL_INSERT, CHAT_WITH_COMMON_URL, COMMON_URL );
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_INSERT, CHAT_WITH_BOTH_URLS, COMMON_URL );
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_INSERT, CHAT_WITH_BOTH_URLS, SINGLE_URL );
	}

	@Transactional
	@Rollback
	@Test
	void removeUnreferencedLink_after_removeChatLink_Test()
	{
		// given

		// when
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_DELETE, CHAT_WITH_BOTH_URLS, SINGLE_URL );

		// then
		assertExists( List.of( COMMON_URL ) );
	}

	@Transactional
	@Rollback
	@Test
	void removeUnreferencedLink_after_removeChat_Test()
	{
		// given

		// when
		jdbcTemplate.update( JdbcChatRepository.SQL_DELETE, CHAT_WITH_BOTH_URLS );

		// then
		assertExists( List.of( COMMON_URL ) );
	}

	@Transactional
	@Rollback
	@Test
	void leaveReferencedLink_after_removeChatLink_Test()
	{
		// given

		// when
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_DELETE, CHAT_WITH_COMMON_URL, COMMON_URL );

		// then
		assertExists( List.of( COMMON_URL, SINGLE_URL ) );
	}

	@Transactional
	@Rollback
	@Test
	void leaveReferencedLink_after_removeChat_Test()
	{
		// given

		// when
		jdbcTemplate.update( JdbcChatRepository.SQL_DELETE, CHAT_WITH_COMMON_URL );

		// then
		assertExists( List.of( COMMON_URL, SINGLE_URL ) );
	}

	private void assertExists( @NonNull List<String> expected )
	{
		List<String> actual = jdbcTemplate
			.query( JdbcLinkRepository.SQL_SELECT_ALL, JdbcLinkRepository.ROW_MAPPER )
			.stream()
			.map( Link::url )
			.map( URI::toString )
			.toList();
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}
}
