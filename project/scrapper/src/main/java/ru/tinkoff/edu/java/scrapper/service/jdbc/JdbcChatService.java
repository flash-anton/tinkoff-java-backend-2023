package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService
{
	private final JdbcChatRepository jdbcChatRepository;

	@Override
	public void add( long tgChatId )
	{
		jdbcChatRepository.add( tgChatId );
	}

	@Override
	public void delete( long tgChatId )
	{
		if( !jdbcChatRepository.remove( tgChatId ) )
		{
			throw new NotFoundException( "Чат не существует", "id == " + tgChatId ); // требует API
		}
	}
}
