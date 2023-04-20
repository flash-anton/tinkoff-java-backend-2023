package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository
{
	public static final String SQL_INSERT = "insert into link (url) values (?) on conflict do nothing";
	public static final String SQL_DELETE = "delete from link where url = ?";
	public static final String SQL_UPDATE = "update link set updated = ? where url = ?";
	public static final String SQL_SELECT_ALL = "select url, updated from link";
	public static final String SQL_SELECT_OLD = "select url, updated from link where updated < ?";

	public static final RowMapper<Link> ROW_MAPPER = ( ResultSet rs, int rowNum ) ->
		new Link( URI.create( rs.getString( "url" ) ), rs.getObject( "updated", OffsetDateTime.class ) );

	private final JdbcTemplate jdbcTemplate;

	@Override
	public boolean add( @NonNull URI url )
	{
		return jdbcTemplate.update( SQL_INSERT, url.toString() ) == 1;
	}

	@Override
	public boolean remove( @NonNull URI url )
	{
		return jdbcTemplate.update( SQL_DELETE, url.toString() ) == 1;
	}

	@Override
	public boolean update( @NonNull URI url, @NonNull OffsetDateTime updated )
	{
		return jdbcTemplate.update( SQL_UPDATE, updated, url.toString() ) == 1;
	}

	@Override
	public @NonNull List<Link> findAll()
	{
		return jdbcTemplate.query( SQL_SELECT_ALL, ROW_MAPPER );
	}

	@Override
	public @NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore )
	{
		return jdbcTemplate.query( SQL_SELECT_OLD, ROW_MAPPER, updatedBefore );
	}
}
