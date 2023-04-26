package ru.tinkoff.edu.java.scrapper.repository;

import lombok.NonNull;

import java.util.List;

public interface ChatRepository
{
	boolean add( long tgChatId );
	boolean remove( long tgChatId );
	@NonNull List<Long> findAll();
}
