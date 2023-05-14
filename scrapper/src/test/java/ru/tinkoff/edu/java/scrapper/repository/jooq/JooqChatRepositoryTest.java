package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepositoryTest;

public class JooqChatRepositoryTest extends ChatRepositoryTest
{
	@Autowired
	public JooqChatRepositoryTest( DSLContext dslContext )
	{
		super( new JooqChatRepository( dslContext ) );
	}
}
