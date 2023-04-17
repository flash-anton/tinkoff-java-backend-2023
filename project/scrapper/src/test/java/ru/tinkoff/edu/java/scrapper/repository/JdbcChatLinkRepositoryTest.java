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
		beforeFindBy();
		long chatId = chatLink.chat_id();

		// when
		List<ChatLink> expected = jdbcChatLinkRepository.findByChatId( chatId );

		// then
		List<ChatLink> actual = jdbcTemplate.query( JdbcChatLinkRepository.SQL_SELECT_BY_CHAT_ID, JdbcChatLinkRepository.ROW_MAPPER, chatId );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	@Transactional
	@Rollback
	@Test
	void findByUrlTest()
	{
		// given
		beforeFindBy();
		String url = chatLink.link_url();

		// when
		List<ChatLink> expected = jdbcChatLinkRepository.findByUrl( url );

		// then
		List<ChatLink> actual = jdbcTemplate.query( JdbcChatLinkRepository.SQL_SELECT_BY_URL, JdbcChatLinkRepository.ROW_MAPPER, url );
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

	private void beforeFindBy()
	{
		List<ChatLink> chatLinks = Stream
			.generate( this::generateChatLink )
			.limit( 2 )
			.peek( this::initTables )
			.peek( this::jdbcTemplateAdd )
			.toList();

		long chatId = chatLinks.get( 0 ).chat_id();
		String url = chatLinks.get( 1 ).link_url();
		chatLink = new ChatLink( chatId, url );
		jdbcTemplateAdd( chatLink );
	}

	private @NotNull ChatLink generateChatLink()
	{
		long chat_id = new Random().nextLong( 1_000_000 );
		String link_url = "url-" + new Random().nextLong( 1_000_000 );
		return new ChatLink( chat_id, link_url );
	}
}
