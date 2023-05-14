package ru.tinkoff.edu.java.scrapper.exception;

import lombok.NonNull;

public class BadRequestException extends ApiErrorException
{
	public BadRequestException( @NonNull String message )
	{
		super( "Некорректные параметры запроса", "400", message );
	}
}
