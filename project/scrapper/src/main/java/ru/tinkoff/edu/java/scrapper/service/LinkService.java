package ru.tinkoff.edu.java.scrapper.service;

import lombok.NonNull;

import java.util.List;

public interface LinkService
{
	void add( long tgChatId, @NonNull String url );
	void delete( long tgChatId, @NonNull String url );
	@NonNull List<String> getUrls( long tgChatId );
}
