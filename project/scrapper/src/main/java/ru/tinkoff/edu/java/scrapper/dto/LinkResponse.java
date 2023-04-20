package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

import java.net.URI;

public record LinkResponse( long id, @NonNull URI url )
{
}
