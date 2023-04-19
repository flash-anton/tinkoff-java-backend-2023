package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@Service
@Qualifier( "JdbcLinkService" )
public class JdbcLinkService extends LinkService
{
	public JdbcLinkService(
		@NonNull @Qualifier( "JdbcChatRepository" ) ChatRepository chatRepository,
		@NonNull @Qualifier( "JdbcChatLinkRepository" ) ChatLinkRepository chatLinkRepository,
		@NonNull @Qualifier( "JdbcLinkRepository" ) LinkRepository linkRepository )
	{
		super( chatRepository, chatLinkRepository, linkRepository );
	}
}
