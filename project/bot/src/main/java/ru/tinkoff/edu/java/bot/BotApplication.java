package ru.tinkoff.edu.java.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.tg.TgBot;
import ru.tinkoff.edu.java.bot.scrapperclient.ScrapperClient;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(BotApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		System.out.println(config);

		TgBot tgBot = ctx.getBean( TgBot.class );
		tgBot.start();

		String url = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
		ScrapperClient scrapperClient = new ScrapperClient( "http://localhost:8080" );
		scrapperClient.addChat( 1 );
		System.out.println( scrapperClient.addLink( 1, url ) );
		System.out.println( scrapperClient.getAllLinks( 1 ) );
		System.out.println( scrapperClient.deleteLink( 1, url ) );
		scrapperClient.deleteChat( 1 );
	}
}
