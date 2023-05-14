package ru.tinkoff.edu.java.bot.scrapperclient.dto;

import lombok.NonNull;

public record ListLinksResponse( @NonNull LinkResponse[] links, int size )
{
}
