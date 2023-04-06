package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TgCommandListTest
{
	@Test
	void severalUrlsInUrlListTest()
	{
		urlsTest( 123461, Set.of( "0", "5", "2", "4", "1", "3" ) );
	}

	@Test
	void singleUrlInUrlListTest()
	{
		urlsTest( 74586345, Set.of( "0" ) );
	}

	@Test
	void emptyUrlListTest()
	{
		urlsTest( 8250756, Set.of() );
	}

	@Test
	void nullUrlListTest()
	{
		urlsTest( 123125, null );
	}

	void urlsTest( long chatId, Set<String> urls )
	{
		// Arrange / Setup
		ApplicationConfig config = new ApplicationConfig( null, null );
		if( urls != null )
		{
			config.getTgUsers().put( chatId, urls );
		}
		TgCommandList cmd = new TgCommandList( config );

		Update update = BotUtils.parseUpdate( "{\"message\"={\"chat\"={\"id\"=" + chatId +"}}}" );


		// Act
		SendMessage msg = cmd.process( update );


		// Assert
		String text = (String)msg.getParameters().get( "text" );

		if( urls != null && !urls.isEmpty() )
		{
			assertEquals( urls, Set.of( text.split( "\n" ) ) );
		}
		else
		{
			assertEquals( "Список отслеживаемых ссылок пуст", text );
		}
	}
}
