package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.entity.JpaLink;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
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
	public boolean update( @NonNull Map<URI, OffsetDateTime> updates )
	{
		if( updates.isEmpty() )
		{
			return false;
		}

		List<JpaLink> links = entityManager
			.createQuery( "select l from JpaLink l where url in :urls", JpaLink.class )
			.setParameter( "urls", updates.keySet().stream().map( URI::toString ).toList() )
			.getResultList();

		links.forEach( jpaLink ->
		{
			URI url = URI.create( jpaLink.getUrl() );
			OffsetDateTime updated = updates.get( url );
			jpaLink.setUpdated( updated );
		} );

		entityManager.flush();

		return links.size() == updates.size();
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

