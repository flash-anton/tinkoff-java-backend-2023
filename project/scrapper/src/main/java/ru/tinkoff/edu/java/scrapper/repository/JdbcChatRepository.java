package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository
{
	public static final String SQL_INSERT = "insert into chat (id) values (?) on conflict do nothing";
	public static final String SQL_DELETE = "delete from chat where id = ?";
	public static final String SQL_SELECT_ALL = "select id from chat";

	private final JdbcTemplate jdbcTemplate;

	public boolean add( long tgChatId )
	{
		return jdbcTemplate.update( SQL_INSERT, tgChatId ) == 1;
	}

	public boolean remove( long tgChatId )
	{
		return jdbcTemplate.update( SQL_DELETE, tgChatId ) == 1;
	}

	public @NonNull List<Long> findAll()
	{
		return jdbcTemplate.queryForList( SQL_SELECT_ALL, Long.class );
	}
}
