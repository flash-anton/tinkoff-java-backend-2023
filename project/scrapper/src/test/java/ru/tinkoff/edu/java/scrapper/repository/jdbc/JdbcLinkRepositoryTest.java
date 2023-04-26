package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepositoryTest;

public class JdbcLinkRepositoryTest extends LinkRepositoryTest
{
	@Autowired
	public JdbcLinkRepositoryTest( JdbcTemplate jdbcTemplate )
	{
		super( new JdbcLinkRepository( jdbcTemplate ) );
	}
}
