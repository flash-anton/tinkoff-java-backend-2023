package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository
{
	private final DSLContext dsl;

	@Override
	public boolean add( long tgChatId )
	{
		return dsl.insertInto( CHAT, CHAT.ID ).values( tgChatId ).onConflictDoNothing().execute() == 1;
	}

	@Override
	public boolean remove( long tgChatId )
	{
		return dsl.deleteFrom( CHAT ).where( CHAT.ID.equal( tgChatId ) ).execute() == 1;
	}

	@Override
	public @NonNull List<Long> findAll()
	{
		return dsl.selectFrom( CHAT ).fetch( CHAT.ID );
	}
}
