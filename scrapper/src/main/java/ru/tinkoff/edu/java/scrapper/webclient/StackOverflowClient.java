package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkChanges;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowQuestionInfoRequest;

import java.time.OffsetDateTime;
import java.util.Arrays;

public record StackOverflowClient( @NonNull WebClient webClient )
{
	public @NonNull StackOverflowQuestionInfoRequest fetchQuestionInfo( @NonNull String questionId, @NonNull OffsetDateTime lastUpdated )
	{
		record StackOverflowQuestionsItem( @NonNull OffsetDateTime creation_date, @NonNull String timeline_type ) {}
		record StackOverflowQuestionsTimeline( @NonNull StackOverflowQuestionsItem[] items ) {}

		StackOverflowQuestionsTimeline response = webClient
			.get()
			.uri( "/questions/{id}/timeline?site=stackoverflow", questionId )
			.retrieve()
			.bodyToMono( StackOverflowQuestionsTimeline.class )
			.block();

		if( response == null )
		{
			response = new StackOverflowQuestionsTimeline( new StackOverflowQuestionsItem[0] );
		}

		LinkChanges linkChanges = new LinkChanges( Arrays
			.stream( response.items )
			.parallel()
			.filter( e -> e.creation_date.isAfter( lastUpdated ) )
			.map( e -> new LinkChanges.Event( e.creation_date, e.timeline_type ) )
			.distinct()
			.toList() );

		return new StackOverflowQuestionInfoRequest( questionId, linkChanges );
	}
}
