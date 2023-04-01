package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.ClientConfiguration;
import ru.tinkoff.edu.java.scrapper.webclient.GitHubClient;
import ru.tinkoff.edu.java.scrapper.webclient.StackOverflowClient;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class, ClientConfiguration.class})
public class ScrapperApplication  {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ScrapperApplication.class, args);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		System.out.println(config);

		ClientConfiguration clientConfiguration = ctx.getBean( ClientConfiguration.class );
		System.out.println( clientConfiguration );

		GitHubClient gitHubClient = ctx.getBean( GitHubClient.class );
		System.out.println( gitHubClient.fetchRepositoryInfo( "flash-anton", "tinkoff-java-backend-2023" ) );

		StackOverflowClient stackOverflowClient = ctx.getBean( StackOverflowClient.class );
		System.out.println( stackOverflowClient.fetchQuestionInfo( "1642028" ) );
	}
}
