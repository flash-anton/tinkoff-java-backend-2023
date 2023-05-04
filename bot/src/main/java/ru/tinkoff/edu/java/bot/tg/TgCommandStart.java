package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;

@Component
@RequiredArgsConstructor
public class TgCommandStart implements TgCommand
{
	private final ScrapperClient scrapperClient;

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
		scrapperClient.addChat( chatId );
		return new SendMessage( chatId, "Вы зарегистрированы" );
	}
}
