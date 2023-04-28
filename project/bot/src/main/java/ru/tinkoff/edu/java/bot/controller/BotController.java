package ru.tinkoff.edu.java.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.TgChatsNotifier;

@RestController
@RequiredArgsConstructor
public class BotController
{
	private final TgChatsNotifier tgChatsNotifier;

	@PostMapping( "/updates" )
	public void linkUpdate( @RequestBody LinkUpdateRequest req )
	{
		// 200, Обновление обработано
		tgChatsNotifier.linkUpdate( req.getDescription(), req.getUrl(), req.getTgChatIds() );
	}
}

