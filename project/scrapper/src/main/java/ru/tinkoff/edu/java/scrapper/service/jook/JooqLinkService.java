package ru.tinkoff.edu.java.scrapper.service.jook;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@Service
@Qualifier( "JooqLinkService" )
public class JooqLinkService extends LinkService
{
	public JooqLinkService(
		@NonNull @Qualifier( "JooqChatRepository" ) ChatRepository chatRepository,
		@NonNull @Qualifier( "JooqChatLinkRepository" ) ChatLinkRepository chatLinkRepository,
		@NonNull @Qualifier( "JooqLinkRepository" ) LinkRepository linkRepository )
	{
		super( chatRepository, chatLinkRepository, linkRepository );
	}
}
