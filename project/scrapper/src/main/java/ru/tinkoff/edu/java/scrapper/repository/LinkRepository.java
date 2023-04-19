package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository
{
	boolean add( @NonNull String url );
	boolean remove( @NonNull String url );
	boolean update( @NonNull String url, @NonNull OffsetDateTime updated );
	@NonNull List<Link> findAll();
	@NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore );
}
