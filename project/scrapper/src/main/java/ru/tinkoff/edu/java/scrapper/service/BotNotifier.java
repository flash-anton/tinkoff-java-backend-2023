package ru.tinkoff.edu.java.scrapper.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public interface BotNotifier
{
	void linkUpdate( @NonNull String description, @NonNull URI url, long[] tgChatIds );
}
