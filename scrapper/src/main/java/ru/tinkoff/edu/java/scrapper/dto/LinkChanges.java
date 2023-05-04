package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record LinkChanges( @NonNull List<Event> events )
{
	public record Event( @NonNull OffsetDateTime time, @NonNull String description ) {}

	public Event last()
	{
		return events
			.parallelStream()
			.max( Comparator.comparing( Event::time ) )
			.orElse( null );
	}

	@Override
	public String toString()
	{
		return events
			.parallelStream()
			.map( e -> e.time + " - " + e.description )
			.sorted( Comparator.reverseOrder() )
			.collect( Collectors.joining( "\n" ) );
	}
}
