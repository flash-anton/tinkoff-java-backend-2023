package ru.tinkoff.edu.java.bot.scrapperclient.exception;

import lombok.NonNull;

public class ApiErrorException extends RuntimeException
{
	public ApiErrorException( @NonNull String message )
	{
		super( message );
	}
}
