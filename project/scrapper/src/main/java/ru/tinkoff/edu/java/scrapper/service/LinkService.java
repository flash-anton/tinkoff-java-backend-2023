package ru.tinkoff.edu.java.scrapper.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public abstract class LinkService
{
	protected final ChatRepository chatRepository;
	protected final ChatLinkRepository chatLinkRepository;
	protected final LinkRepository linkRepository;

	@Transactional
	public void add( long tgChatId, @NonNull String url )
	{
		chatRepository.add( tgChatId ); // вместо исключения
		linkRepository.add( url );
		chatLinkRepository.add( tgChatId, url );
	}

	public void delete( long tgChatId, @NonNull String url )
	{
		if( !chatLinkRepository.remove( tgChatId, url ) )
		{
			throw new NotFoundException( "Ссылка не найдена", "url == " + url ); // требует API
		}
	}

	public void update( @NonNull String url, @NonNull OffsetDateTime updated )
	{
		linkRepository.update( url, updated );
	}

	public @NonNull List<String> getUrls( long tgChatId )
	{
		return chatLinkRepository.findByChatId( tgChatId ).stream().map( ChatLink::link_url ).toList();
	}

	public @NonNull List<Long> getChats( @NonNull String url )
	{
		return chatLinkRepository.findByUrl( url ).stream().map( ChatLink::chat_id ).toList();
	}

	public @NonNull List<Link> getLinks( @NonNull OffsetDateTime updatedBefore )
	{
		return linkRepository.findOld( updatedBefore );
	}
}
