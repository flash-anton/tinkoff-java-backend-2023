package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.HashSet;

@Component
public class TgCommandStart implements TgCommand
{
	private final ApplicationConfig config;

	public TgCommandStart( ApplicationConfig config )
	{
		this.config = config;
	}

	@Override
	public String name()
	{
		return "/start";
	}

	@Override
	public String description()
	{
		return "зарегистрировать пользователя";
	}

	@Override
	public SendMessage process( Update update )
	{
		long chatId = update.message().chat().id();
		config.getTgUsers().putIfAbsent( chatId, new HashSet<>() );
		return new SendMessage( chatId, "Вы зарегистрированы" );
	}
}
