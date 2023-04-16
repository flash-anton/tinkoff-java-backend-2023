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

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationEnvironment
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcChatRepository jdbcChatRepository;
	private long chatId;

	@BeforeEach
	void beforeEach()
	{
		chatId = new Random().nextLong( 1_000_000 );
	}

	@Transactional
	@Rollback
	@Test
	void addIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcChatRepository.add( chatId );

		// then
		assertTrue( result );
		assertExists( chatId );
	}

	@Transactional
	@Rollback
	@Test
	void addIfExistsTest()
	{
		// given
		jdbcTemplateAdd( chatId );

		// when
		boolean result = jdbcChatRepository.add( chatId );

		// then
		assertFalse( result );
		assertExists( chatId );
	}

	@Transactional
	@Rollback
	@Test
	void removeIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcChatRepository.remove( chatId );

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
		jdbcTemplateAdd( chatId );

		// when
		boolean result = jdbcChatRepository.remove( chatId );

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
		new Random().longs( 100 ).forEach( this::jdbcTemplateAdd );

		// when
		List<Long> expected = jdbcChatRepository.findAll();

		// then
		assertExists( expected );
	}

	private void assertExists( Long... expected )
	{
		assertExists( List.of( expected ) );
	}

	private void assertExists( @NotNull List<Long> expected )
	{
		List<Long> actual = jdbcTemplate.queryForList( JdbcChatRepository.SQL_SELECT_ALL, Long.class );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void jdbcTemplateAdd( long chatId )
	{
		jdbcTemplate.update( JdbcChatRepository.SQL_INSERT, chatId );
	}
}
