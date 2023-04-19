package ru.tinkoff.edu.java.scrapper.service.jook;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Service
@Qualifier( "JooqChatService" )
public class JooqChatService extends ChatService
{
	public JooqChatService( @NonNull @Qualifier( "JooqChatRepository" ) ChatRepository chatRepository )
	{
		super( chatRepository );
	}
}
