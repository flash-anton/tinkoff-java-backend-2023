package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepositoryTest;

public class JpaLinkRepositoryTest extends LinkRepositoryTest
{
	@Autowired
	public JpaLinkRepositoryTest( EntityManager entityManager )
	{
		super( new JpaLinkRepository( entityManager ) );
	}
}
