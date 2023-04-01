package ru.tinkoff.edu.java.scrapper.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorResponse
{
	private @NonNull String description;
	private @NonNull String code;
	private @NonNull String exceptionName;
	private @NonNull String exceptionMessage;
	private @NonNull String[] stacktrace;
}
