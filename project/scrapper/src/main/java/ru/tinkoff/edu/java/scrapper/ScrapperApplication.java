package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.ClientConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfiguration.class})
public class ScrapperApplication  {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ScrapperApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		System.out.println(config);

		ClientConfiguration clientConfiguration = ctx.getBean( ClientConfiguration.class );
		System.out.println( clientConfiguration );
	}
}
