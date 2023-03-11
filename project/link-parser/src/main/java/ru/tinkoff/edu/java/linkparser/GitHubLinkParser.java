package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

/**
 * Парсер ссылки с GitHub, который возвращает пару пользователь/репозиторий.
 */
public class GitHubLinkParser extends HierarchicalLinkParser
{
	/**
	 * @param next следующий парсер иерархической ссылки в Цепочке Обязанностей.
	 */
	public GitHubLinkParser( @Nullable HierarchicalLinkParser next )
	{
		super( next );
	}

	@Override
	protected @NotNull LinkContent parseImpl( @NotNull HierarchicalLinkContent hlc )
	{
		if( hlc.authority().equals( "github.com" ) &&
			(hlc.path().getNameCount() >= 2) )
		{
			String user = hlc.path().getName( 0 ).toString();
			String repository = hlc.path().getName( 1 ).toString();
			return new GitHubLinkContent( user, repository );
		}
		return new UnsupportedLinkContent();
	}
}
