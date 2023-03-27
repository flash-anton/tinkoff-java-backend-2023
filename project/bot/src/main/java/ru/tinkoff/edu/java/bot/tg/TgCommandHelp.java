package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TgCommandHelp implements TgCommand
{
	private String help;

	public void setHelp( String help )
	{
		this.help = help;
	}

	@Override
	public String name()
	{
		return "/help";
	}

	@Override
	public String description()
	{
		return "вывести окно с командами";
	}

	@Override
	public SendMessage process( Update update )
	{
		long chatId = update.message().chat().id();
		return new SendMessage( chatId, help );
	}
}
