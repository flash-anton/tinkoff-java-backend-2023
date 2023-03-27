package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface TgCommand
{
	String name();
	String description();
	SendMessage process( Update update );

	default boolean isSupported( Update update )
	{
		return update.message().text().split( " " )[0].equals( name() );
	}

	default BotCommand toBotCommand()
	{
		return new BotCommand( name(), description() );
	}
}
