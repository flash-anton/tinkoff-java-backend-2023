package ru.tinkoff.edu.java.scrapper.entity;

import lombok.NonNull;

import java.time.OffsetDateTime;

public record Link( @NonNull String url, @NonNull OffsetDateTime updated )
{
}
