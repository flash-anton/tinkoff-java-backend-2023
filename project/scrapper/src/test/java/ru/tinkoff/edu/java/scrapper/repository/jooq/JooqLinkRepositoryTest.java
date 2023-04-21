package ru.tinkoff.edu.java.scrapper.repository.jooq;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepositoryTest;

public class JooqLinkRepositoryTest extends LinkRepositoryTest
{
	@Autowired
	public JooqLinkRepositoryTest( DSLContext dslContext )
	{
		super( new JooqLinkRepository( dslContext ) );
	}
}
