package ru.tinkoff.edu.java.scrapper.entity;

import lombok.NonNull;

public record ChatLink( long chat_id, @NonNull String link_url )
{
}
