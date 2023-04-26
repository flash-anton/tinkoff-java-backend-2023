package ru.tinkoff.edu.java.scrapper.entity;

import lombok.NonNull;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link( @NonNull URI url, @NonNull OffsetDateTime updated )
{
}
