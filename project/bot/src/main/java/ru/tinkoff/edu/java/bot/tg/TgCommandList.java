package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.Set;

@Component
public class TgCommandList implements TgCommand
{
	private final ApplicationConfig config;

	public TgCommandList( ApplicationConfig config )
	{
		this.config = config;
	}

	@Override
	public String name()
	{
		return "/list";
	}

	@Override
	public String description()
	{
		return "показать список отслеживаемых ссылок";
	}

	@Override
	public SendMessage process( Update update )
	{
		long chatId = update.message().chat().id();

		Set<String> urls = config.getTgUsers().get( chatId );
		if( urls == null || urls.isEmpty() )
		{
			return new SendMessage( chatId, "Список отслеживаемых ссылок пуст" );
		}
		return new SendMessage( chatId, String.join( "\n", urls ) );
	}
}
