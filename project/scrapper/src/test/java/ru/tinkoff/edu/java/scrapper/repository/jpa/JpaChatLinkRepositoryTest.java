package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepositoryTest;

public class JpaChatLinkRepositoryTest extends ChatLinkRepositoryTest
{
	@Autowired
	public JpaChatLinkRepositoryTest( EntityManager entityManager )
	{
		super( new JpaChatLinkRepository( entityManager ) );
	}
}
