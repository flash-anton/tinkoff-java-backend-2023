package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.LinkResponse;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TgCommandList implements TgCommand
{
	private final ScrapperClient scrapperClient;

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

		List<String> urls = Arrays
			.stream( scrapperClient.getAllLinks( chatId ).links() )
			.map( LinkResponse::url )
			.map( URI::toString )
			.toList();

		if( urls.isEmpty() )
		{
			return new SendMessage( chatId, "Список отслеживаемых ссылок пуст" );
		}
		return new SendMessage( chatId, String.join( "\n", urls ) );
	}
}
