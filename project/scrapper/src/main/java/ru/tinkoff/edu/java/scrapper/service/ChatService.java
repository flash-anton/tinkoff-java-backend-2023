package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

@RequiredArgsConstructor
public abstract class ChatService
{
	protected final ChatRepository chatRepository;

	public void add( long tgChatId )
	{
		chatRepository.add( tgChatId );
	}

	public void delete( long tgChatId )
	{
		if( !chatRepository.remove( tgChatId ) )
		{
			throw new NotFoundException( "Чат не существует", "id == " + tgChatId ); // требует API
		}
	}
}
