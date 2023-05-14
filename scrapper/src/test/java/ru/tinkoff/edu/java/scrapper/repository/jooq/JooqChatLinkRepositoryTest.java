package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepositoryTest;

public class JooqChatLinkRepositoryTest extends ChatLinkRepositoryTest
{
	@Autowired
	public JooqChatLinkRepositoryTest( DSLContext dslContext )
	{
		super( new JooqChatLinkRepository( dslContext ) );
	}
}
