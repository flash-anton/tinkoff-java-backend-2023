package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

/**
 * Парсер иерархической ссылки формата [scheme:][//authority][path][?query][#fragment] в Цепочке Обязанностей.
 */
public class HierarchicalLinkParser
{
	/**
	 * Следующий парсер иерархической ссылки в Цепочке Обязанностей.
	 */
	private final @Nullable HierarchicalLinkParser next;

	/**
	 * @param next следующий парсер иерархической ссылки в Цепочке Обязанностей.
	 */
	public HierarchicalLinkParser( @Nullable HierarchicalLinkParser next )
	{
		this.next = next;
	}

	/**
	 * Парсинг иерархической ссылки Цепочкой парсеров.
	 * @param url ссылка.
	 * @return содержимое ссылки.
	 */
	public final @NotNull LinkContent parse( @NotNull URI url )
	{
		String scheme = url.getScheme();
		String authority = url.getAuthority();
		String path = url.getPath();
		if( (scheme == null) || (authority == null) || (path == null) )
		{
			return new UnsupportedLinkContent();
		}

		String query = url.getQuery();
		String fragment = url.getFragment();
		HierarchicalLinkContent hlc = new HierarchicalLinkContent( scheme, authority, Path.of( path ), query, fragment );

		return parse( hlc );
	}

	/**
	 * Парсинг содержимого иерархической ссылки Цепочкой парсеров.
	 * @param hlc содержимое иерархической ссылки.
	 * @return содержимое конкретной иерархической ссылки.
	 */
	private @NotNull LinkContent parse( @NotNull HierarchicalLinkContent hlc )
	{
		if( isSupportedImpl( hlc ) )
		{
			return parseImpl( hlc );
		}
		if( next != null )
		{
			return next.parse( hlc );
		}
		return new UnsupportedLinkContent();
	}

	/**
	 * Реализация проверки поддержки парсинга содержимого иерархической ссылки.
	 * @param hlc содержимое иерархической ссылки.
	 * @return true если поддерживается.
	 */
	protected boolean isSupportedImpl( @NotNull HierarchicalLinkContent hlc )
	{
		return true;
	}

	/**
	 * Реализация парсинга содержимого иерархической ссылки.
	 * @param hlc содержимое иерархической ссылки.
	 * @return содержимое конкретной иерархической ссылки.
	 */
	protected @NotNull LinkContent parseImpl( @NotNull HierarchicalLinkContent hlc )
	{
		return hlc;
	}
}
