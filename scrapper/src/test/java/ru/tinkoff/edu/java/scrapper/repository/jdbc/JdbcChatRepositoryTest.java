package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepositoryTest;

public class JdbcChatRepositoryTest extends ChatRepositoryTest
{
	@Autowired
	public JdbcChatRepositoryTest( JdbcTemplate jdbcTemplate )
	{
		super( new JdbcChatRepository( jdbcTemplate ) );
	}
}
