package ru.tinkoff.edu.java.bot.scrapperclient.dto;

import lombok.NonNull;

import java.net.URI;

public record AddLinkRequest( @NonNull URI link )
{
}
