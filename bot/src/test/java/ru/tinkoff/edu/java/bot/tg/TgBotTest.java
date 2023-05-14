package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TgBotTest
{
	@Test
	void unknownCommandTest()
	{
		// Arrange / Setup
		TgBot tgBot = new TgBot( null, new ArrayList<>() );
		Update update = BotUtils.parseUpdate( "{\"message\"={\"text\"=\"/unknownCommand\",\"chat\"={\"id\"=0}}}" );

		// Act
		SendMessage msg = tgBot.processUpdate( update );

		// Assert
		String text = (String)msg.getParameters().get( "text" );
		assertEquals( "Команда не поддерживается", text );
	}
}
