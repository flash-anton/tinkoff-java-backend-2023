package ru.tinkoff.edu.java.scrapper.entity;

import lombok.NonNull;

import java.net.URI;

public record ChatLink( long chat_id, @NonNull URI link_url )
{
}
