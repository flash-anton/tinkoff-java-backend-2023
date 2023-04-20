package ru.tinkoff.edu.java.bot.scrapperclient.dto;

import lombok.NonNull;

import java.net.URI;

public record RemoveLinkRequest( @NonNull URI link )
{
}
