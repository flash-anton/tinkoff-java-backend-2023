package ru.tinkoff.edu.java.linkparser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.linkparser.LinkContent.GitHubLinkContent;
import ru.tinkoff.edu.java.linkparser.LinkContent.StackOverflowLinkContent;
import ru.tinkoff.edu.java.linkparser.LinkContent.UnsupportedLinkContent;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinkParserTest
{
	private static LinkParser firstParser;

	@BeforeAll
	static void initParserChain()
	{
		firstParser = new StackOverflowLinkParser();
		firstParser.setNext( new GitHubLinkParser() )
				   .setNext( new GitHubLinkParser() ); // так добавлять остальные парсеры в Цепочку
	}

	@Test
	void githubLinkParsingTest()
	{
		linkParsingTest( "https://github.com/sanyarnd/tinkoff-java-course-2022/", new GitHubLinkContent( "sanyarnd", "tinkoff-java-course-2022" ) );
	}

	@Test
	void stackoverflowLinkParsingTest()
	{
		linkParsingTest( "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c", new StackOverflowLinkContent( "1642028" ) );
	}

	@Test
	void unsupportedLink1ParsingTest()
	{
		linkParsingTest( "https://stackoverflow.com/search?q=unsupported%20link", new UnsupportedLinkContent() );
	}

	@Test
	void unsupportedLink2ParsingTest()
	{
		linkParsingTest( "mailto:java-net@www.example.com", new UnsupportedLinkContent() );
	}

	void linkParsingTest( String link, LinkContent expectedLinkContent )
	{
		// Arrange / Setup

		// Act
		LinkContent linkContent = firstParser.parse( URI.create( link ) );

		// Assert
		assertEquals( expectedLinkContent, linkContent );
	}
}
