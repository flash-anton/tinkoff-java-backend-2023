package ru.tinkoff.edu.java.scrapper.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService
{
	private final ChatRepository chatRepository;
	private final ChatLinkRepository chatLinkRepository;
	private final LinkRepository linkRepository;

	@Transactional
	public void add( long tgChatId, @NonNull URI url )
	{
		chatRepository.add( tgChatId ); // вместо исключения
		linkRepository.add( url );
		chatLinkRepository.add( tgChatId, url );
	}

	public void delete( long tgChatId, @NonNull URI url )
	{
		if( !chatLinkRepository.remove( tgChatId, url ) )
		{
			throw new NotFoundException( "Ссылка не найдена", "url == " + url ); // требует API
		}
	}

	public void update( @NonNull URI url, @NonNull OffsetDateTime updated )
	{
		linkRepository.update( url, updated );
	}

	public @NonNull List<URI> getUrls( long tgChatId )
	{
		return chatLinkRepository.findByChatId( tgChatId ).stream().map( ChatLink::link_url ).toList();
	}

	public @NonNull List<Long> getChats( @NonNull URI url )
	{
		return chatLinkRepository.findByUrl( url ).stream().map( ChatLink::chat_id ).toList();
	}

	public @NonNull List<Link> getLinks( @NonNull OffsetDateTime updatedBefore )
	{
		return linkRepository.findOld( updatedBefore );
	}
}
