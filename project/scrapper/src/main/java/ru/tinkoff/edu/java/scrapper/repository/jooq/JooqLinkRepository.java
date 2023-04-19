package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINK;

@Repository
@RequiredArgsConstructor
@Qualifier( "JooqLinkRepository" )
public class JooqLinkRepository implements LinkRepository
{
	public static final RecordMapper<LinkRecord, Link> RECORD_MAPPER = record ->
		new Link( record.getUrl(), record.getUpdated() );

	private final DSLContext dsl;

	@Override
	public boolean add( @NonNull String url )
	{
		return dsl.insertInto( LINK, LINK.URL ).values( url ).onConflictDoNothing().execute() == 1;
	}

	@Override
	public boolean remove( @NonNull String url )
	{
		return dsl.deleteFrom( LINK ).where( LINK.URL.equal( url ) ).execute() == 1;
	}

	@Override
	public boolean update( @NonNull String url, @NonNull OffsetDateTime updated )
	{
		return dsl.update( LINK ).set( LINK.UPDATED, updated ).where( LINK.URL.equal( url ) ).execute() == 1;
	}

	@Override
	public @NonNull List<Link> findAll()
	{
		return dsl.selectFrom( LINK ).fetch( RECORD_MAPPER );
	}

	@Override
	public @NonNull List<Link> findOld( @NonNull OffsetDateTime updatedBefore )
	{
		return dsl.selectFrom( LINK ).where( LINK.UPDATED.lessThan( updatedBefore ) ).fetch( RECORD_MAPPER );
	}
}