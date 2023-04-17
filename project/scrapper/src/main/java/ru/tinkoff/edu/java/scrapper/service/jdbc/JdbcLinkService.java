package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService
{
	private final JdbcChatRepository jdbcChatRepository;
	private final JdbcChatLinkRepository jdbcChatLinkRepository;
	private final JdbcLinkRepository jdbcLinkRepository;

	@Override
	@Transactional
	public void add( long tgChatId, @NonNull String url )
	{
		jdbcChatRepository.add( tgChatId ); // вместо исключения
		jdbcLinkRepository.add( url );
		jdbcChatLinkRepository.add( tgChatId, url );
	}

	@Override
	@Transactional
	public void delete( long tgChatId, @NonNull String url )
	{
		if( !jdbcChatLinkRepository.remove( tgChatId, url ) )
		{
			throw new NotFoundException( "Ссылка не найдена", "url == " + url );
		}
	}

	@Override
	public @NonNull List<String> getUrls( long tgChatId )
	{
		return jdbcChatLinkRepository.findByChatId( tgChatId ).stream().map( ChatLink::link_url ).toList();
	}
}
