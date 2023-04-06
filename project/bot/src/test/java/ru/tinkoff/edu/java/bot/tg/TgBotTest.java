package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TgBotTest
{
	@Test
	void unknownCommandTest()
	{
		// Arrange / Setup
		ApplicationConfig config = new ApplicationConfig( null, null );
		TgBot tgBot = new TgBot( config, new ArrayList<>() );
		Update update = BotUtils.parseUpdate( "{\"message\"={\"text\"=\"/unknownCommand\",\"chat\"={\"id\"=0}}}" );

		// Act
		SendMessage msg = tgBot.processUpdate( update );

		// Assert
		String text = (String)msg.getParameters().get( "text" );
		assertEquals( "Команда не поддерживается", text );
	}
}
