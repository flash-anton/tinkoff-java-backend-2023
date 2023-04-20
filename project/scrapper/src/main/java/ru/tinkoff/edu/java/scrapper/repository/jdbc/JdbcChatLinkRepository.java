package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.tinkoff.edu.java.scrapper.entity.ChatLink;
import ru.tinkoff.edu.java.scrapper.repository.ChatLinkRepository;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository
{
	public static final String SQL_INSERT = "insert into chat_link (chat_id, link_url) values (?,?) on conflict do nothing";
	public static final String SQL_DELETE = "delete from chat_link where (chat_id = ?) and (link_url = ?)";
	public static final String SQL_SELECT_ALL = "select chat_id, link_url from chat_link";
	public static final String SQL_SELECT_BY_CHAT_ID = "select chat_id, link_url from chat_link where chat_id = ?";
	public static final String SQL_SELECT_BY_URL = "select chat_id, link_url from chat_link where link_url = ?";

	public static final RowMapper<ChatLink> ROW_MAPPER = ( ResultSet rs, int rowNum ) ->
		new ChatLink( rs.getLong( "chat_id" ), rs.getString( "link_url" ) );

	private final JdbcTemplate jdbcTemplate;

	@Override
	public boolean add( long tgChatId, @NonNull String url )
	{
		return jdbcTemplate.update( SQL_INSERT, tgChatId, url ) == 1;
	}

	@Override
	public boolean remove( long tgChatId, @NonNull String url )
	{
		return jdbcTemplate.update( SQL_DELETE, tgChatId, url ) == 1;
	}

	@Override
	public @NonNull List<ChatLink> findAll()
	{
		return jdbcTemplate.query( SQL_SELECT_ALL, ROW_MAPPER );
	}

	@Override
	public @NonNull List<ChatLink> findByChatId( long tgChatId )
	{
		return jdbcTemplate.query( SQL_SELECT_BY_CHAT_ID, ROW_MAPPER, tgChatId );
	}

	@Override
	public @NonNull List<ChatLink> findByUrl( @NonNull String url )
	{
		return jdbcTemplate.query( SQL_SELECT_BY_URL, ROW_MAPPER, url );
	}
}
