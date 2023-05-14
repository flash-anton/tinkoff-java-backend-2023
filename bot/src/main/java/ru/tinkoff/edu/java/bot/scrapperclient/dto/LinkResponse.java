package ru.tinkoff.edu.java.bot.scrapperclient.dto;

import lombok.NonNull;

import java.net.URI;

public record LinkResponse( long id, @NonNull URI url )
{
}
