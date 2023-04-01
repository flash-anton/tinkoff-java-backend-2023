package ru.tinkoff.edu.java.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;

import java.util.Arrays;

@RestControllerAdvice
class BotExceptionHandler
{
	@ExceptionHandler( ApiErrorException.class )
	@ResponseStatus( value = HttpStatus.BAD_REQUEST )
	public ApiErrorResponse apiErrorExceptionHandler( ApiErrorException ex )
	{
		String[] stacktrace = Arrays.stream( ex.getStackTrace() )
									.map( StackTraceElement::toString )
									.toArray( String[]::new );

		ApiErrorResponse r = new ApiErrorResponse();
		r.setDescription( "Некорректные параметры запроса" );
		r.setCode( "400" );
		r.setExceptionName( ex.getClass().getSimpleName() );
		r.setExceptionMessage( ex.getMessage() );
		r.setStacktrace( stacktrace );
		return r;
	}
}
