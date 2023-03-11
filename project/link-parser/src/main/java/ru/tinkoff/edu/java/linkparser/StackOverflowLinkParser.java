package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

/**
 * Парсер ссылки со StackOverflow, который возвращает id вопроса.
 */
public class StackOverflowLinkParser extends HierarchicalLinkParser
{
	/**
	 * @param next следующий парсер иерархической ссылки в Цепочке Обязанностей.
	 */
	public StackOverflowLinkParser( @Nullable HierarchicalLinkParser next )
	{
		super( next );
	}

	@Override
	protected @NotNull LinkContent parseImpl( @NotNull HierarchicalLinkContent hlc )
	{
		if( hlc.authority().equals( "stackoverflow.com" ) &&
			(hlc.path().getNameCount() >= 2) &&
			hlc.path().getName( 0 ).toString().equals( "questions" ) )
		{
			String questionId = hlc.path().getName( 1 ).toString();
			return new StackOverflowLinkContent( questionId );
		}
		return new UnsupportedLinkContent();
	}
}
