package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

import ru.tinkoff.edu.java.linkparser.LinkContent.*;

/**
 * Абстрактный парсер ссылки в Цепочке Обязанностей.
 */
public abstract class LinkParser
{
	/**
	 * Следующий парсер ссылки в Цепочке Обязанностей.
	 */
	private @Nullable LinkParser next;

	/**
	 * Задать следующий парсер ссылки.
	 * @param next следующий парсер ссылки.
	 * @return this.
	 */
	public final LinkParser setNext( @Nullable LinkParser next )
	{
		this.next = next;
		return this;
	}

	/**
	 * Парсинг ссылки Цепочкой парсеров.
	 * @param url ссылка.
	 * @return содержимое ссылки.
	 */
	public final @NotNull LinkContent parse( @NotNull URI url )
	{
		if( isSupported( url ) )
		{
			return parseImpl( url );
		}
		if( next != null )
		{
			return next.parse( url );
		}
		return new UnsupportedLinkContent();
	}

	/**
	 * Проверка поддержки парсинга ссылки.
	 * @param url ссылка.
	 * @return true если поддерживается.
	 */
	protected abstract boolean isSupported( @NotNull URI url );

	/**
	 * Реализация парсинга ссылки.
	 * @param url ссылка.
	 * @return содержимое ссылки.
	 */
	protected abstract @NotNull LinkContent parseImpl( @NotNull URI url );
}
