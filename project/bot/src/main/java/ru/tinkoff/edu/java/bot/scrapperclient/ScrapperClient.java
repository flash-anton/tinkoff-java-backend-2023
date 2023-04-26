package ru.tinkoff.edu.java.bot.scrapperclient;

import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.scrapperclient.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.scrapperclient.exception.ApiErrorException;

import java.net.URI;

public class ScrapperClient
{
	protected final WebClient client;

	public ScrapperClient( @NonNull URI baseUrl )
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
			}))
			.baseUrl( baseUrl.toString() )
			.build();
	}

	public void addChat( long id )
	{
		client.post()
			  .uri( "/tg-chat/{id}", id )
			  .retrieve()
			  .bodyToMono( Void.class )
			  .block();
	}

	public void deleteChat( long id )
	{
		client.delete()
			  .uri( "/tg-chat/{id}", id )
			  .retrieve()
			  .bodyToMono( Void.class )
			  .block();
	}

	public ListLinksResponse getAllLinks( long id )
	{
		return client.get()
					 .uri( "/links", id )
					 .header( "Tg-Chat-Id", String.valueOf( id ) )
					 .retrieve()
					 .bodyToMono( ListLinksResponse.class )
					 .block();
	}

	public LinkResponse addLink( long id, URI url )
	{
		return client.post()
					 .uri( "/links", id )
					 .header( "Tg-Chat-Id", String.valueOf( id ) )
					 .body( BodyInserters.fromValue( new AddLinkRequest( url ) ) )
					 .retrieve()
					 .bodyToMono( LinkResponse.class )
					 .block();
	}

	public LinkResponse deleteLink( long id, URI url )
	{
		return client.method( HttpMethod.DELETE )
					 .uri( "/links", id )
					 .header( "Tg-Chat-Id", String.valueOf( id ) )
					 .body( BodyInserters.fromValue( new RemoveLinkRequest( url ) ) )
					 .retrieve()
					 .bodyToMono( LinkResponse.class )
					 .block();
	}
}
