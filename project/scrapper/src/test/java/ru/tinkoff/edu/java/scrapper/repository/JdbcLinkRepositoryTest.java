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
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcLinkRepository jdbcLinkRepository;
	private String url;

	@BeforeEach
	void beforeEach()
	{
		url = "url-" + new Random().nextLong( 1_000_000 );
	}

	@Transactional
	@Rollback
	@Test
	void addIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcLinkRepository.add( url );

		// then
		assertTrue( result );
		assertExists( url );
	}

	@Transactional
	@Rollback
	@Test
	void addIfExistsTest()
	{
		// given
		jdbcTemplateAdd( url );

		// when
		boolean result = jdbcLinkRepository.add( url );

		// then
		assertFalse( result );
		assertExists( url );
	}

	@Transactional
	@Rollback
	@Test
	void removeIfNotExistsTest()
	{
		// given

		// when
		boolean result = jdbcLinkRepository.remove( url );

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
		jdbcTemplateAdd( url );

		// when
		boolean result = jdbcLinkRepository.remove( url );

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
		Stream.generate( () -> "url-" + new Random().nextLong( 1_000_000 ) )
			  .limit( 100 )
			  .forEach( this::jdbcTemplateAdd );

		// when
		List<Link> expected = jdbcLinkRepository.findAll();

		// then
		assertExists( expected.stream().map( Link::url ).toList() );
	}

	private void assertExists( String... expected )
	{
		assertExists( List.of( expected ) );
	}

	private void assertExists( @NotNull List<String> expected )
	{
		List<String> actual = jdbcTemplate.query( JdbcLinkRepository.SQL_SELECT_ALL, JdbcLinkRepository.ROW_MAPPER )
										  .stream().map( Link::url ).toList();
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void jdbcTemplateAdd( String url )
	{
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, url );
	}
}
