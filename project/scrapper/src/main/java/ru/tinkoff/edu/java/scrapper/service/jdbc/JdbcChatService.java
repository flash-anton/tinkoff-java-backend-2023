package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Service
@Qualifier( "JdbcChatService" )
public class JdbcChatService extends ChatService
{
	public JdbcChatService( @NonNull @Qualifier( "JdbcChatRepository" ) ChatRepository chatRepository )
	{
		super( chatRepository );
	}
}
