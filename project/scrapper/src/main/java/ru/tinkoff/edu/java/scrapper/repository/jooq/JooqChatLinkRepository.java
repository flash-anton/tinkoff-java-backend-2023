package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.ChatLinkRecord;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;

import java.net.URI;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;

@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository
{
	public static final RecordMapper<ChatLinkRecord, ChatLink> RECORD_MAPPER = record ->
		new ChatLink( record.getChatId(), URI.create( record.getLinkUrl() ) );

	private final DSLContext dsl;

	@Override
	public boolean add( long tgChatId, @NonNull URI url )
	{
		return 1 == dsl
			.insertInto( CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_URL )
			.values( tgChatId, url.toString() )
			.onConflictDoNothing()
			.execute();
	}

	@Override
	public boolean remove( long tgChatId, @NonNull URI url )
	{
		return 1 == dsl
			.deleteFrom( CHAT_LINK )
			.where( CHAT_LINK.CHAT_ID.equal( tgChatId ), CHAT_LINK.LINK_URL.equal( url.toString() ) )
			.execute();
	}

	@Override
	public @NonNull List<ChatLink> findAll()
	{
		return dsl.selectFrom( CHAT_LINK ).fetch( RECORD_MAPPER );
	}

	@Override
	public @NonNull List<ChatLink> findByChatId( long tgChatId )
	{
		return dsl.selectFrom( CHAT_LINK ).where( CHAT_LINK.CHAT_ID.equal( tgChatId ) ).fetch( RECORD_MAPPER );
	}

	@Override
	public @NonNull List<ChatLink> findByUrl( @NonNull URI url )
	{
		return dsl.selectFrom( CHAT_LINK ).where( CHAT_LINK.LINK_URL.equal( url.toString() ) ).fetch( RECORD_MAPPER );
	}
}

