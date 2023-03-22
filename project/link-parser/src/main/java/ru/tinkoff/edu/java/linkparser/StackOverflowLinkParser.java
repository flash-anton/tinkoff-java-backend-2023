package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

import java.net.URI;

/**
 * Парсер ссылки со StackOverflow, который возвращает id вопроса.
 */
public class StackOverflowLinkParser extends LinkParser
{
	@Override
	protected boolean isSupported( @NotNull URI url )
	{
		if( (url.getAuthority() == null) || (url.getPath() == null) )
		{
			return false;
		}

		String[] path = url.getPath().split( "/" );

		return url.getAuthority().equals( "stackoverflow.com" ) &&
			   path.length >= 3 &&
			   path[1].equals( "questions" );
	}

	@Override
	protected @NotNull LinkContent parseImpl( @NotNull URI url )
	{
		String questionId = url.getPath().split( "/" )[2];
		return new StackOverflowLinkContent( questionId );
	}
}
