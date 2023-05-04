package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

import java.net.URI;

public record AddLinkRequest( @NonNull URI link )
{
}
