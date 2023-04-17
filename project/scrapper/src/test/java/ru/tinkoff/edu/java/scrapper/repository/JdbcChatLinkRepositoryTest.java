package ru.tinkoff.edu.java.scrapper.repository;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcChatLinkRepository jdbcChatLinkRepository;
	private ChatLink chatLink;

	@BeforeEach
	void beforeEach()
	{
		chatLink = generateChatLink();
		initTables( chatLink );
	}

	@Transactional
	@Rollback
	@Test
	void addIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcChatLinkRepository.add( chatLink.chat_id(), chatLink.link_url() );

		// then
		assertTrue( result );
		assertExists( chatLink );
	}

	@Transactional
	@Rollback
	@Test
	void addIfExistsTest()
	{
		// given
		jdbcTemplateAdd( chatLink );

		// when
		boolean result = jdbcChatLinkRepository.add( chatLink.chat_id(), chatLink.link_url() );

		// then
		assertFalse( result );
		assertExists( chatLink );
	}

	@Transactional
	@Rollback
	@Test
	void removeIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcChatLinkRepository.remove( chatLink.chat_id(), chatLink.link_url() );

		// then
		assertFalse( result );
		assertExists();
	}

	@Transactional
	@Rollback
	@Test
	void removeIfExistsTest()
	{
		// given
		jdbcTemplateAdd( chatLink );

		// when
		boolean result = jdbcChatLinkRepository.remove( chatLink.chat_id(), chatLink.link_url() );

		// then
		assertTrue( result );
		assertExists();
	}

	@Transactional
	@Rollback
	@Test
	void findAllTest()
	{
		// given
		Stream.generate( this::generateChatLink )
			  .limit( 100 )
			  .peek( this::initTables )
			  .forEach( this::jdbcTemplateAdd );

		// when
		List<ChatLink> expected = jdbcChatLinkRepository.findAll();

		// then
		assertExists( expected );
	}

	@Transactional
	@Rollback
	@Test
	void findByChatIdTest()
	{
		// given
		ChatLink chat1Link1 = generateChatLink();
		initTables( chat1Link1 );
		jdbcTemplateAdd( chat1Link1 );

		ChatLink chat2Link2 = generateChatLink();
		initTables( chat2Link2 );
		jdbcTemplateAdd( chat2Link2 );

		ChatLink chat1Link2 = new ChatLink( chat1Link1.chat_id(), chat2Link2.link_url() );
		jdbcTemplateAdd( chat1Link2 );

		// when
		List<ChatLink> expected = jdbcChatLinkRepository.findByChatId( chat1Link2.chat_id() );

		// then
		assertEquals( expected.size(), 2 );
		assertTrue( expected.contains( chat1Link1 ) );
		assertTrue( expected.contains( chat1Link2 ) );

		List<ChatLink> actual = jdbcTemplate.query( JdbcChatLinkRepository.SQL_SELECT_BY_CHAT_ID, JdbcChatLinkRepository.ROW_MAPPER, chat1Link2.chat_id() );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void assertExists( ChatLink... expected )
	{
		assertExists( List.of( expected ) );
	}

	private void assertExists( @NotNull List<ChatLink> expected )
	{
		List<ChatLink> actual = jdbcTemplate.query( JdbcChatLinkRepository.SQL_SELECT_ALL, JdbcChatLinkRepository.ROW_MAPPER );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void jdbcTemplateAdd( @NotNull ChatLink chatLink )
	{
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_INSERT, chatLink.chat_id(), chatLink.link_url() );
	}

	private void initTables( @NotNull ChatLink chatLink )
	{
		jdbcTemplate.update( JdbcChatRepository.SQL_INSERT, chatLink.chat_id() );
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, chatLink.link_url() );
	}

	private @NotNull ChatLink generateChatLink()
	{
		long chat_id = new Random().nextLong( 1_000_000 );
		String link_url = "url-" + new Random().nextLong( 1_000_000 );
		return new ChatLink( chat_id, link_url );
	}
}
