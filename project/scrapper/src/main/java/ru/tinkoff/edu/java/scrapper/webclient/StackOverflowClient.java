package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestionInfoRequest;

public record StackOverflowClient( @NonNull WebClient webClient )
{
	public StackOverflowQuestionInfoRequest fetchQuestionInfo( String questionId )
	{
		record StackOverflowQuestionsRequest( @NonNull StackOverflowQuestionInfoRequest[] items ){}

		return webClient
			.get()
			.uri( "/questions/{id}?site=stackoverflow", questionId )
			.retrieve()
			.bodyToMono( StackOverflowQuestionsRequest.class )
			.block()
			.items()[0];
	}
}
