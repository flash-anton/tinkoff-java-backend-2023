package ru.tinkoff.edu.java.bot;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.tg.TgBot;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(BotApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		config = new ApplicationConfig( config.test(), "***", config.scrapperBaseUrl() );
		LogManager.getLogger().info(config);

		TgBot tgBot = ctx.getBean( TgBot.class );
		tgBot.start();
	}
}
