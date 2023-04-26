package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

public record StackOverflowQuestionInfoRequest( @NonNull String questionId, @NonNull LinkChanges linkChanges )
{
}
