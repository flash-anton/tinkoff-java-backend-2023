package ru.tinkoff.edu.java.scrapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.OffsetDateTime;

@Configuration
@EnableScheduling
public class LinkUpdaterScheduler
{
	@Scheduled( fixedDelayString = "#{@schedulerIntervalMs}" )
	public void update()
	{
		System.out.println( "LinkUpdaterScheduler.update(): " + OffsetDateTime.now() );
	}
}
