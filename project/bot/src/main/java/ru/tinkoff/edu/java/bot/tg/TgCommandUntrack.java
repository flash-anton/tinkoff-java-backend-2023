package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

@Component
public class TgCommandUntrack implements TgCommand
{
	private final ApplicationConfig config;

	public TgCommandUntrack( ApplicationConfig config )
	{
		this.config = config;
	}

	@Override
	public String name()
	{
		return "/untrack";
	}

	@Override
	public String description()
	{
		return "прекратить отслеживание ссылки";
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

		String url = parts[1];
		config.getTgUsers().compute( chatId, (k,v) ->
		{
			if( v == null )
			{
				return null;
			}
			v.removeIf( url::equals );
			return v;
		});

		return new SendMessage( chatId, "Отслеживание ссылки прекращено" );
	}
}
