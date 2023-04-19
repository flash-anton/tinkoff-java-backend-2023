package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;

import java.util.List;

public interface ChatLinkRepository
{
	boolean add( long tgChatId, @NonNull String url );
	boolean remove( long tgChatId, @NonNull String url );
	@NonNull List<ChatLink> findAll();
	@NonNull List<ChatLink> findByChatId( long tgChatId );
	@NonNull List<ChatLink> findByUrl( @NonNull String url );
}
