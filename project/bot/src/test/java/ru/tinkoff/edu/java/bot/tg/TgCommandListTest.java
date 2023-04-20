package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TgCommandListTest
{
	@Test
	void severalUrlsInUrlListTest()
	{
		urlsTest( 123461, Set.of( 0, 5, 2, 4, 1, 3 ) );
	}

	@Test
	void singleUrlInUrlListTest()
	{
		urlsTest( 74586345, Set.of( 0 ) );
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

	void urlsTest( long chatId, Set<Integer> ports )
	{
		// Arrange / Setup
		Set<URI> urls = new HashSet<>();
		if( ports != null && !ports.isEmpty() )
		{
			urls = ports
				.parallelStream()
				.map( port -> URI.create( "http://localhost:" + port ) )
				.collect( Collectors.toSet() );
		}

		ScrapperClient scrapperClient = new ScrapperClient( URI.create( "http://localhost:8080" ) );
		scrapperClient.addChat( chatId );
		urls.forEach( url -> scrapperClient.addLink( chatId, url ) );
		TgCommandList cmd = new TgCommandList( scrapperClient );

		Update update = BotUtils.parseUpdate( "{\"message\"={\"chat\"={\"id\"=" + chatId + "}}}" );


		// Act
		SendMessage msg = cmd.process( update );


		// Assert
		String text = (String)msg.getParameters().get( "text" );

		if( urls.isEmpty() )
		{
			assertEquals( "Список отслеживаемых ссылок пуст", text );
		}
		else
		{
			assertEquals( urls, Arrays.stream( text.split( "\n" ) ).map( URI::create ).collect( Collectors.toSet() ) );
		}
	}
}
