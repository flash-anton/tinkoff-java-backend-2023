package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository
{
	public static final String SQL_INSERT = "insert into link (url) values (?) on conflict do nothing";
	public static final String SQL_DELETE = "delete from link where url = ?";
	public static final String SQL_UPDATE = "update link set updated = ? where url = ?";
	public static final String SQL_SELECT_ALL = "select url, updated from link";
	public static final String SQL_SELECT_OLD = "select url, updated from link where updated < ?";

	public static final RowMapper<Link> ROW_MAPPER = ( ResultSet rs, int rowNum ) ->
	{
		String url = rs.getString( "url" );
		OffsetDateTime updated = rs.getObject( "updated", OffsetDateTime.class );
		return new Link( url, updated );
	};

	private final JdbcTemplate jdbcTemplate;

	public boolean add( @NonNull String url )
	{
		return jdbcTemplate.update( SQL_INSERT, url ) == 1;
	}

	public boolean remove( @NonNull String url )
	{
		return jdbcTemplate.update( SQL_DELETE, url ) == 1;
	}

	public boolean update( @NonNull String url, @NonNull OffsetDateTime updated )
	{
		return jdbcTemplate.update( SQL_UPDATE, updated, url ) == 1;
	}

	public @NonNull List<Link> findAll()
	{
		return jdbcTemplate.query( SQL_SELECT_ALL, ROW_MAPPER );
	}

	public @NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore )
	{
		return jdbcTemplate.query( SQL_SELECT_OLD, ROW_MAPPER, updatedBefore );
	}
}
