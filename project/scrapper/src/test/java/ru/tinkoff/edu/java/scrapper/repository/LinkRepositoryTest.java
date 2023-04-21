package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public abstract class LinkRepositoryTest extends IntegrationEnvironment
{
	private final LinkRepository linkRepository;
	private URI url;

	@BeforeEach
	void beforeEach()
	{
		url = URI.create( "url-" + new Random().nextLong( 1_000_000 ) );
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
			  .map( URI::create )
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
			  .map( URI::create )
			  .peek( this::jdbcTemplateAdd )
			  .forEach( url ->
			  {
				  int year = new Random().nextInt( 1970, 2023 );
				  jdbcTemplateUpdate( url, OffsetDateTime.parse( year + "-04-18T02:09:00+00:00" ) );
			  } );

		OffsetDateTime updatedBefore = OffsetDateTime.parse( "2000-04-18T02:09:00+00:00" );

		// when
		List<Link> expected = linkRepository.findOld( updatedBefore );

		// then
		List<Link> actual = jdbcTemplate.query( JdbcLinkRepository.SQL_SELECT_OLD, JdbcLinkRepository.ROW_MAPPER, updatedBefore );
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void assertExists( URI... expected )
	{
		assertExists( List.of( expected ) );
	}

	private void assertExists( @NotNull List<URI> expected )
	{
		List<URI> actual = jdbcTemplate
			.query( JdbcLinkRepository.SQL_SELECT_ALL, JdbcLinkRepository.ROW_MAPPER )
			.stream()
			.map( Link::url )
			.toList();
		assertEquals( expected.size(), actual.size() );
		assertTrue( expected.containsAll( actual ) );
	}

	private void jdbcTemplateAdd( @NonNull URI url )
	{
		jdbcTemplate.update( JdbcLinkRepository.SQL_INSERT, url.toString() );
	}

	private void jdbcTemplateUpdate( @NonNull URI url, @NonNull OffsetDateTime updated )
	{
		jdbcTemplate.update( JdbcLinkRepository.SQL_UPDATE, updated, url.toString() );
	}
}
