package ru.tinkoff.edu.java.scrapper.service;

import lombok.NonNull;
import ru.tinkoff.edu.java.scrapper.entity.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService
{
	void add( long tgChatId, @NonNull String url );
	void delete( long tgChatId, @NonNull String url );
	void update( @NonNull String url, @NonNull OffsetDateTime updated );

	@NonNull List<String> getUrls( long tgChatId );
	@NonNull List<Long> getChats( @NonNull String url );

	@NonNull List<Link> getLinks( @NonNull OffsetDateTime updatedBefore );
}
