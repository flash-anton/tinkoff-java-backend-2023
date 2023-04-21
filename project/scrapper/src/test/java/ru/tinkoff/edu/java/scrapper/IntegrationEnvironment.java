package ru.tinkoff.edu.java.scrapper;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
@ContextConfiguration( classes = TestConfig.class )
public abstract class IntegrationEnvironment
{
	@Autowired
	public JdbcTemplate jdbcTemplate;

	static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

	static
	{
		POSTGRESQL_CONTAINER = new PostgreSQLContainer<>( "postgres:15" );
		POSTGRESQL_CONTAINER.start();

		runMigration();
	}

	static void runMigration()
	{
		String url = POSTGRESQL_CONTAINER.getJdbcUrl();
		String user = POSTGRESQL_CONTAINER.getUsername();
		String pass = POSTGRESQL_CONTAINER.getPassword();

		Path path = Path.of( "." ).toAbsolutePath().getParent().resolve( "migrations/" );

		try( Connection connection = DriverManager.getConnection( url, user, pass );
			 DirectoryResourceAccessor directoryResourceAccessor = new DirectoryResourceAccessor( path ) )
		{
			JdbcConnection jdbcConnection = new JdbcConnection( connection );
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation( jdbcConnection );
			Liquibase liquibase = new liquibase.Liquibase( "master.xml", directoryResourceAccessor, database );
			liquibase.update( new Contexts(), new LabelExpression() );
		}
		catch( Exception ex )
		{
			throw new RuntimeException( ex );
		}
	}
}
