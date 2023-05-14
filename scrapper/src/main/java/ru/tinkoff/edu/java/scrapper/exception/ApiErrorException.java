package ru.tinkoff.edu.java.scrapper.exception;

import lombok.Getter;
import lombok.NonNull;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

import java.util.Arrays;

@Getter
public abstract class ApiErrorException extends RuntimeException
{
	private final @NonNull String description;
	private final @NonNull String code;

	public ApiErrorException( @NonNull String description, @NonNull String code, @NonNull String message )
	{
		super( message );
		this.description = description;
		this.code = code;
	}

	public @NonNull ApiErrorResponse toResponse()
	{
		String[] stacktrace = Arrays.stream( getStackTrace() )
									.map( StackTraceElement::toString )
									.toArray( String[]::new );

		ApiErrorResponse r = new ApiErrorResponse();
		r.setDescription( description );
		r.setCode( code );
		r.setExceptionName( this.getClass().getSimpleName() );
		r.setExceptionMessage( getMessage() );
		r.setStacktrace( stacktrace );
		return r;
	}
}
