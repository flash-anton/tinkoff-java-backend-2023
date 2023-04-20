package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;
import ru.tinkoff.edu.java.scrapper.domain.jooq.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.entity.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINK;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository
{
	public static final RecordMapper<LinkRecord, Link> RECORD_MAPPER = record ->
		new Link( URI.create( record.getUrl() ), record.getUpdated().atZoneSameInstant( ZoneId.of( "Z" ) ).toOffsetDateTime() );

	private final DSLContext dsl;

	@Override
	public boolean add( @NonNull URI url )
	{
		return dsl.insertInto( LINK, LINK.URL ).values( url.toString() ).onConflictDoNothing().execute() == 1;
	}

	@Override
	public boolean remove( @NonNull URI url )
	{
		return dsl.deleteFrom( LINK ).where( LINK.URL.equal( url.toString() ) ).execute() == 1;
	}

	@Override
	public boolean update( @NonNull URI url, @NonNull OffsetDateTime updated )
	{
		return dsl.update( LINK ).set( LINK.UPDATED, updated ).where( LINK.URL.equal( url.toString() ) ).execute() == 1;
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