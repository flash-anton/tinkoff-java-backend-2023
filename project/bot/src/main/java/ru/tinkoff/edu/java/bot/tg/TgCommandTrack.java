package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.HashSet;

public class TgCommandTrack implements TgCommand
{
	private final ApplicationConfig config;

	public TgCommandTrack( ApplicationConfig config )
	{
		this.config = config;
	}

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

		String url = parts[1];
		config.getTgUsers().compute( chatId, (k,v) ->
		{
			if( v == null )
			{
				v = new HashSet<>(); // регистрация пользователя вместо исключения
			}
			v.add( url );
			return v;
		});

		return new SendMessage( chatId, "Отслеживание ссылки начато" );
	}
}
