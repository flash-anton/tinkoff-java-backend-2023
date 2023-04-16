package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationEnvironmentTest extends IntegrationEnvironment
{
    @Test
    void isContainerStartedTest()
    {
        assertTrue( POSTGRESQL_CONTAINER.isCreated() );
    }
}
