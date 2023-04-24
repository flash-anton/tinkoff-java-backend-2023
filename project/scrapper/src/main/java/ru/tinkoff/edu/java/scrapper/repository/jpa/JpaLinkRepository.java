package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.entity.JpaLink;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository
{
	private static final Function<JpaLink, Link> MAPPER = j ->
		new Link( URI.create( j.getUrl() ), j.getUpdated().atZoneSameInstant( ZoneId.of( "Z" ) ).toOffsetDateTime() );

	private final EntityManager entityManager;

	@Override
	public boolean add( @NonNull URI url )
	{
		return entityManager
				   .createNativeQuery( "insert into link (url) values (:url) on conflict do nothing" )
				   .setParameter( "url", url.toString() )
				   .executeUpdate() == 1;
	}

	@Override
	public boolean remove( @NonNull URI url )
	{
		return entityManager
				   .createQuery( "delete from JpaLink where url = :url" )
				   .setParameter( "url", url.toString() )
				   .executeUpdate() == 1;
	}

	@Override
	@Transactional
	public boolean update( @NonNull URI url, @NonNull OffsetDateTime updated )
	{
		return entityManager
				   .createQuery( "update JpaLink set updated = :updated where url = :url" )
				   .setParameter( "updated", updated )
				   .setParameter( "url", url.toString() )
				   .executeUpdate() == 1;
	}

	@Override
	public @NonNull List<Link> findAll()
	{
		return entityManager
			.createQuery( "select l from JpaLink l", JpaLink.class )
			.getResultList()
			.stream()
			.map( MAPPER )
			.toList();
	}

	@Override
	public @NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore )
	{
		return entityManager
			.createQuery( "select l from JpaLink l where updated < :updated", JpaLink.class )
			.setParameter( "updated", updatedBefore )
			.getResultList()
			.stream()
			.map( MAPPER )
			.toList();
	}
}

