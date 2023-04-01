package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.exception.ApiErrorException;

@RestController
public class BotController
{
	@PostMapping( "/updates" )
	public void linkUpdate( @RequestBody LinkUpdateRequest req )
	{
		switch( (int)req.getId() )
		{
			case 400 -> throw new ApiErrorException( "id == 400" );
		}
		// 200, Обновление обработано
	}
}

