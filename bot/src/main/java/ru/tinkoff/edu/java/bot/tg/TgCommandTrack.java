package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class TgCommandTrack implements TgCommand
{
	private final ScrapperClient scrapperClient;

	@Override
	public String name()
	{
		return "/track";
	}

	@Override
	public String description()
	{
		return "начать отслеживание ссылки";
	}

	@Override
	public SendMessage process( Update update )
	{
		long chatId = update.message().chat().id();

		String[] parts = update.message().text().split( " " );
		if( parts.length == 1 )
		{
			return new SendMessage( chatId, "Синтаксис команды: " + name() + " url" );
		}

		URI url = URI.create( parts[1] );
		scrapperClient.addLink( chatId, url );
		return new SendMessage( chatId, "Отслеживание ссылки начато" );
	}
}
