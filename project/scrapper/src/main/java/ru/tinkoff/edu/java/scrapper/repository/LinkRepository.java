package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository
{
	boolean add( @NonNull URI url );
	boolean remove( @NonNull URI url );
	boolean update( @NonNull URI url, @NonNull OffsetDateTime updated );
	@NonNull List<Link> findAll();
	@NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore );
}
