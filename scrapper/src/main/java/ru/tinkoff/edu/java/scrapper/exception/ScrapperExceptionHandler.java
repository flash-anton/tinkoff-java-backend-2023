package ru.tinkoff.edu.java.scrapper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;

@RestControllerAdvice
public class ScrapperExceptionHandler
{
	@ExceptionHandler( BadRequestException.class )
	@ResponseStatus( value = HttpStatus.BAD_REQUEST )
	public ApiErrorResponse badRequestExceptionHandler( BadRequestException ex )
	{
		return ex.toResponse();
	}

	@ExceptionHandler( NotFoundException.class )
	@ResponseStatus( value = HttpStatus.NOT_FOUND )
	public ApiErrorResponse notFoundExceptionHandler( NotFoundException ex )
	{
		return ex.toResponse();
	}
}
