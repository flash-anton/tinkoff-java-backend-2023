package ru.tinkoff.edu.java.bot.exception;

import lombok.NonNull;

public class ApiErrorException extends RuntimeException
{
	public ApiErrorException( @NonNull String message )
	{
		super( message );
	}
}
