package ru.tinkoff.edu.java.linkparser;

import java.net.URI;
import java.util.stream.Stream;

/**
 * Пример использования цепочки парсеров для разбора ссылок из задания.
 */
public class UseLinkParser
{
	public static void main( String[] args )
	{
		LinkParser firstParser = new StackOverflowLinkParser();
		firstParser.setNext( new GitHubLinkParser() )
				   .setNext( new GitHubLinkParser() ); // так добавлять остальные парсеры в Цепочку

		Stream.of(
			"https://github.com/sanyarnd/tinkoff-java-course-2022/",
			"https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
			"https://stackoverflow.com/search?q=unsupported%20link",
			"mailto:java-net@www.example.com"
		)
		.peek( System.out::println )
		.map( URI::create )
		.map( firstParser::parse )
		.forEach( System.out::println );
	}
}
