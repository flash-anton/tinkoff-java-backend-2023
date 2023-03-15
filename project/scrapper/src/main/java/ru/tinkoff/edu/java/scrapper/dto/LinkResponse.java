package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

public record LinkResponse( long id, @NonNull String url )
{
}
