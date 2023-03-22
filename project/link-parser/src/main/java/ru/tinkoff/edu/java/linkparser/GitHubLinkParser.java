package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

import java.net.URI;

/**
 * Парсер ссылки с GitHub, который возвращает пару пользователь/репозиторий.
 */
public class GitHubLinkParser extends LinkParser
{
	@Override
	protected boolean isSupported( @NotNull URI url )
	{
		if( (url.getAuthority() == null) || (url.getPath() == null) )
		{
			return false;
		}

		return url.getAuthority().equals( "github.com" ) &&
			   url.getPath().split( "/" ).length >= 3;
	}

	@Override
	protected @NotNull LinkContent parseImpl( @NotNull URI url )
	{
		String[] path = url.getPath().split( "/" );
		String user = path[1];
		String repository = path[2];
		return new GitHubLinkContent( user, repository );
	}
}
