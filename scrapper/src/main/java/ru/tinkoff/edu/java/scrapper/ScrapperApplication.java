package ru.tinkoff.edu.java.scrapper;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication  {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ScrapperApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		LogManager.getLogger().info(config);
	}
}
