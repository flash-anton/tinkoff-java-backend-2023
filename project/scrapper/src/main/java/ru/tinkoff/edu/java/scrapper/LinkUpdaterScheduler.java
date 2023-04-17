package ru.tinkoff.edu.java.scrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class LinkUpdaterScheduler
{
	private final JdbcTemplate jdbcTemplate;

	@Scheduled( fixedDelayString = "#{@schedulerIntervalMs}" )
	public void update()
	{
		List<String> rows = jdbcTemplate.query( """
				select * from chat c
				full join chat_link cl on c.id = cl.chat_id
				full join link l on cl.link_url = l.url
				order by c.id
				""",
			( rs, rowNum ) -> String.join( " | ",
				String.valueOf( rs.getLong( "id" ) ),
				String.valueOf( rs.getLong( "chat_id" ) ),
				rs.getString( "link_url" ),
				rs.getString( "url" ),
				String.valueOf( rs.getTimestamp( "updated" ) )
			)
		);

		System.out.printf( "LinkUpdaterScheduler.update(): %s\n%s\n",
			OffsetDateTime.now(), String.join( "\n", rows ) );
	}
}