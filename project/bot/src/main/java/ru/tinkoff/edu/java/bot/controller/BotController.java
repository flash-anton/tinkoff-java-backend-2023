package ru.tinkoff.edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.exception.ApiErrorException;
import ru.tinkoff.edu.java.bot.tg.TgBot;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class BotController
{
	private final TgBot tgBot;

	@PostMapping( "/updates" )
	public void linkUpdate( @RequestBody LinkUpdateRequest req )
	{
		if( req.getId() == 400 )
		{
			throw new ApiErrorException( "id == 400" );
		}
		// 200, Обновление обработано
		Arrays.stream( req.getTgChatIds() ).parallel().forEach( chatId ->
		{
			SendMessage msg = new SendMessage( chatId, req.getUrl() + "\n" + req.getDescription() );
			tgBot.sendMessage( msg );
		});
	}
}

