package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepositoryTest;

public class JdbcChatLinkRepositoryTest extends ChatLinkRepositoryTest
{
	@Autowired
	public JdbcChatLinkRepositoryTest( JdbcTemplate jdbcTemplate )
	{
		super( new JdbcChatLinkRepository( jdbcTemplate ) );
	}
}
