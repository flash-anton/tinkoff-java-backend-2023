package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.entity.JpaChatLink;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class JpaChatLinkRepository implements ChatLinkRepository
{
	private static final Function<JpaChatLink, ChatLink> MAPPER = j ->
		new ChatLink( j.getChat_id(), URI.create( j.getLink_url() ) );

	private final EntityManager entityManager;

	@Override
	public boolean add( long tgChatId, @NonNull URI url )
	{
		return entityManager
				   .createNativeQuery( "insert into chat_link (chat_id, link_url) values (:id, :url) on conflict do nothing" )
				   .setParameter( "id", tgChatId )
				   .setParameter( "url", url.toString() )
				   .executeUpdate() == 1;
	}

	@Override
	public boolean remove( long tgChatId, @NonNull URI url )
	{
		return entityManager
				   .createQuery( "delete from JpaChatLink where (chat_id = :id) and (link_url = :url)" )
				   .setParameter( "id", tgChatId )
				   .setParameter( "url", url.toString() )
				   .executeUpdate() == 1;
	}

	@Override
	public @NonNull List<ChatLink> findAll()
	{
		return entityManager
			.createQuery( "select cl from JpaChatLink cl", JpaChatLink.class )
			.getResultList()
			.stream()
			.map( MAPPER )
			.toList();
	}

	@Override
	public @NonNull List<ChatLink> findByChatId( long tgChatId )
	{
		return entityManager
			.createQuery( "select cl from JpaChatLink cl where chat_id = :id", JpaChatLink.class )
			.setParameter( "id", tgChatId )
			.getResultList()
			.stream()
			.map( MAPPER )
			.toList();
	}

	@Override
	public @NonNull List<ChatLink> findByUrl( @NonNull URI url )
	{
		return entityManager
			.createQuery( "select cl from JpaChatLink cl where link_url = :url", JpaChatLink.class )
			.setParameter( "url", url.toString() )
			.getResultList()
			.stream()
			.map( MAPPER )
			.toList();
	}
}
