package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;
import java.time.OffsetDateTime;

public record StackOverflowQuestionInfoRequest( @NonNull String link, @NonNull OffsetDateTime last_activity_date )
{
}
