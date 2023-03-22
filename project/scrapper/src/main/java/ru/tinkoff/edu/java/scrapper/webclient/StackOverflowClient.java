package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestionInfoRequest;

import java.util.List;

public class StackOverflowClient
{
	protected final WebClient client;

	public StackOverflowClient()
	{
		this( null );
	}

	public StackOverflowClient( String baseUrl )
	{
		HttpClient httpClient = HttpClient.create().compress( true );

		client = WebClientBuilder.create( baseUrl, "https://api.stackexchange.com/2.3" )
								 .defaultHeader( "Accept", "application/json" )
								 .defaultHeader( "Accept-Encoding", "gzip" )
								 .clientConnector( new ReactorClientHttpConnector( httpClient ) )
								 .build();
	}

	public StackOverflowQuestionInfoRequest fetchQuestionInfo( String questionId )
	{
		record StackOverflowQuestionsRequest( @NonNull List< StackOverflowQuestionInfoRequest > items ){}

		return client.get()
					 .uri( "/questions/{id}?site=stackoverflow", questionId )
					 .retrieve()
					 .bodyToMono( StackOverflowQuestionsRequest.class )
					 .block()
					 .items()
					 .get( 0 );
	}
}
