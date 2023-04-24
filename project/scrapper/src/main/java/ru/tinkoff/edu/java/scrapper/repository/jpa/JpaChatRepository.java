package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.entity.JpaChat;

import java.util.List;

@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository
{
	private final EntityManager entityManager;

	@Override
	@Transactional
	public boolean add( long tgChatId )
	{
		return entityManager
				   .createNativeQuery( "insert into chat (id) values (:id) on conflict do nothing" )
				   .setParameter( "id", tgChatId )
				   .executeUpdate() == 1;
	}

	@Override
	public boolean remove( long tgChatId )
	{
		return entityManager
				   .createQuery( "delete from JpaChat where id = :id" )
				   .setParameter( "id", tgChatId )
				   .executeUpdate() == 1;
	}

	@Override
	public @NonNull List<Long> findAll()
	{
		return entityManager
			.createQuery( "select c from JpaChat c", JpaChat.class )
			.getResultList()
			.stream()
			.map( JpaChat::getId )
			.toList();
	}
}
