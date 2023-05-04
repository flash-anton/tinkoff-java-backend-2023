package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepositoryTest;

public class JpaChatRepositoryTest extends ChatRepositoryTest
{
	@Autowired
	public JpaChatRepositoryTest( EntityManager entityManager )
	{
		super( new JpaChatRepository( entityManager ) );
	}
}
