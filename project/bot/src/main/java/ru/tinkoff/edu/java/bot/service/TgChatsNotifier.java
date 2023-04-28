package ru.tinkoff.edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.exception.ApiErrorException;
import ru.tinkoff.edu.java.bot.tg.TgBot;

import java.net.URI;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TgChatsNotifier
{
	private final TgBot tgBot;

	public void linkUpdate( @NonNull String description, @NonNull URI url, long[] tgChatIds )
	{
		if( tgChatIds.length == 0 )
		{
			throw new ApiErrorException( "id == 400" );
		}
		Arrays.stream( tgChatIds ).parallel().forEach( chatId ->
			tgBot.sendMessage( new SendMessage( chatId, url + "\n" + description ) ) );
	}
}
