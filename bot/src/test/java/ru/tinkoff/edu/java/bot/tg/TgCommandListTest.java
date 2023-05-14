package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.ListLinksResponse;

import java.net.URI;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TgCommandListTest
{
	@ParameterizedTest
	@ValueSource( ints = {0, 1, 10} )
	void correctAnswerOnDifferentUrlsNumber( int urlsNumber )
	{
		// given
		long chatId = new Random().nextInt( 1_000_000 );

		Set<URI> urls = IntStream
			.range( 0, urlsNumber )
			.mapToObj( i -> URI.create( "http://localhost:" + i ) )
			.collect( Collectors.toSet() );

		LinkResponse[] linkResponseArray = urls
			.stream()
			.map( url -> new LinkResponse( chatId, url ) )
			.toArray( LinkResponse[]::new );

		ListLinksResponse listLinksResponse = new ListLinksResponse( linkResponseArray, linkResponseArray.length );

		ScrapperClient scrapperClient = Mockito.mock( ScrapperClient.class );
		Mockito.when( scrapperClient.getAllLinks( Mockito.eq( chatId ) ) ).thenReturn( listLinksResponse );

		TgCommandList cmd = new TgCommandList( scrapperClient );

		Update update = BotUtils.parseUpdate( "{\"message\"={\"chat\"={\"id\"=" + chatId + "}}}" );


		// when
		SendMessage msg = cmd.process( update );


		// then
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
