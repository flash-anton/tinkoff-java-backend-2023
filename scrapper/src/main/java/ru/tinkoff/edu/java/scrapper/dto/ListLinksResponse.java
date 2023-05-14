package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

public record ListLinksResponse( @NonNull LinkResponse[] links, int size )
{
}
