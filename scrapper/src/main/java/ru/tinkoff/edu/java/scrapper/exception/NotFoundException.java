package ru.tinkoff.edu.java.scrapper.exception;

import lombok.NonNull;

public class NotFoundException extends ApiErrorException
{
	public NotFoundException( @NonNull String description, @NonNull String message )
	{
		super( description, "404", message );
	}
}
