package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LinkRepositoryTest extends IntegrationEnvironment
{
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
		boolean result = linkRepository.add( url );

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
		boolean result = linkRepository.add( url );

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
		boolean result = linkRepository.remove( url );

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
		boolean result = linkRepository.remove( url );

		// then
		assertTrue( result );
		assertExists();
	}

	@Transactional
	@Rollback
	@Test
	void updateIfNotExistsTest()
	{
		// given
		OffsetDateTime updated = OffsetDateTime.now();

		// when
		boolean result = linkRepository.update( url, updated );

		// then
		assertFalse( result );

		List<Link> actual = jdbcTemplate.query( JdbcLinkRepository.SQL_SELECT_ALL, JdbcLinkRepository.ROW_MAPPER );
		assertTrue( actual.isEmpty() );
	}

	@Transactional
	@Rollback
	@Test
	void updateIfExistsTest()
	{
		// given
		jdbcTemplateAdd( url );
		OffsetDateTime updated = OffsetDateTime.parse( "2023-04-18T02:09:00+00:00" );

		// when
		boolean result = linkRepository.update( url, updated );

		// then
		assertTrue( result );

		List<Link> actual = jdbcTemplate.query( JdbcLinkRepository.SQL_SELECT_ALL, JdbcLinkRepository.ROW_MAPPER );
		assertEquals( List.of( new Link( url, updated ) ), actual );
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
		List<Link> expected = linkRepository.findAll();

		// then
		assertExists( expected.stream().map( Link::url ).toList() );
	}

	@Transactional
	@Rollback
	@Test
	void findOldTest()
	{
		// given
		Stream.generate( () -> "url-" + new Random().nextLong( 1_000_000 ) )
			  .limit( 100 )
			  .peek( this::jdbcTemplateAdd )
			  .forEach( url ->
				  IntStream.range( 1970, 2023 ).forEach( year ->
					  jdbcTemplateUpdate( url, OffsetDateTime.parse( year + "-04-18T02:09:00+00:00" ) ) ) );

		OffsetDateTime updatedBefore = OffsetDateTime.parse( "2000-04-18T02:09:00+00:00" );

		// when
		List<Link> expected = linkRepository.findOld( updatedBefore );

		// then
		List<Link> actual = jdbcTemplate.query( JdbcLinkRepository.SQL_SELECT_OLD, JdbcLinkRepository.ROW_MAPPER, updatedBefore );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
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

	private void jdbcTemplateAdd( @NonNull String url )
	{
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, url );
	}

	private void jdbcTemplateUpdate( @NonNull String url, @NonNull OffsetDateTime updated )
	{
		jdbcTemplate.update( JdbcLinkRepository.SQL_UPDATE, updated, url );
	}
}
