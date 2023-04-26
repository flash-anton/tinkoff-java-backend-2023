package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration
@ComponentScan( "ru.tinkoff.edu.java.scrapper.repository" )
public class TestConfig
{
	@Bean
	public DataSource dataSource()
	{
		PostgreSQLContainer<?> db = IntegrationEnvironment.POSTGRESQL_CONTAINER;

		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass( db.getJdbcDriverInstance().getClass() );
		dataSource.setUrl( db.getJdbcUrl() );
		dataSource.setUsername( db.getUsername() );
		dataSource.setPassword( db.getPassword() );
		return dataSource;
	}
}
