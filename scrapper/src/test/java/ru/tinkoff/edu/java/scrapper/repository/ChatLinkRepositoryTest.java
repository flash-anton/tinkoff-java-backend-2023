package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public abstract class ChatLinkRepositoryTest extends IntegrationEnvironment
{
	private final ChatLinkRepository chatLinkRepository;
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
		boolean result = chatLinkRepository.add( chatLink.chat_id(), chatLink.link_url() );

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
		boolean result = chatLinkRepository.add( chatLink.chat_id(), chatLink.link_url() );

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
		boolean result = chatLinkRepository.remove( chatLink.chat_id(), chatLink.link_url() );

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
		boolean result = chatLinkRepository.remove( chatLink.chat_id(), chatLink.link_url() );

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
		List<ChatLink> expected = chatLinkRepository.findAll();

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
		List<ChatLink> expected = chatLinkRepository.findByChatId( chatId );

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
		URI url = chatLink.link_url();

		// when
		List<ChatLink> expected = chatLinkRepository.findByUrl( url );

		// then
		List<ChatLink> actual = jdbcTemplate.query( JdbcChatLinkRepository.SQL_SELECT_BY_URL, JdbcChatLinkRepository.ROW_MAPPER, url.toString() );
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
		jdbcTemplate.update( JdbcChatLinkRepository.SQL_INSERT, chatLink.chat_id(), chatLink.link_url().toString() );
	}

	private void initTables( @NotNull ChatLink chatLink )
	{
		jdbcTemplate.update( JdbcChatRepository.SQL_INSERT, chatLink.chat_id() );
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, chatLink.link_url().toString() );
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
		URI url = chatLinks.get( 1 ).link_url();
		chatLink = new ChatLink( chatId, url );
		jdbcTemplateAdd( chatLink );
	}

	private @NotNull ChatLink generateChatLink()
	{
		long chat_id = new Random().nextLong( 1_000_000 );
		URI link_url = URI.create( "url-" + new Random().nextLong( 1_000_000 ) );
		return new ChatLink( chat_id, link_url );
	}
}
