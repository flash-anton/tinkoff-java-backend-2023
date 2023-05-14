package ru.tinkoff.edu.java.scrapper.botclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.botclient.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.botclient.exception.ApiErrorException;

import java.net.URI;

public class BotClient
{
	protected final WebClient client;

	public BotClient( @NonNull URI baseUrl )
	{
		client = WebClient
			.builder()
			.filter( ExchangeFilterFunction.ofResponseProcessor( clientResponse ->
			{
				if( clientResponse.statusCode().is4xxClientError() )
				{
					return clientResponse
						.bodyToMono( String.class )
						.flatMap( body -> Mono.error( new ApiErrorException( body ) ) );
				}
				return Mono.just( clientResponse );
			} ) )
			.baseUrl( baseUrl.toString() )
			.build();
	}

	public void linkUpdate( @NonNull String description, @NonNull URI url, long[] tgChatIds )
	{
		LinkUpdateRequest req = new LinkUpdateRequest();
		req.setUrl( url );
		req.setDescription( description );
		req.setTgChatIds( tgChatIds );

		client.post()
			  .uri( "/updates" )
			  .body( BodyInserters.fromValue( req ) )
			  .retrieve()
			  .bodyToMono( Void.class )
			  .block();
	}
}
