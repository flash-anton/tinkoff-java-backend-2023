package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * Содержимое ссылки.
 */
public sealed interface LinkContent permits LinkContent.GitHubLinkContent, LinkContent.HierarchicalLinkContent, LinkContent.StackOverflowLinkContent, LinkContent.UnsupportedLinkContent
{
	/**
	 * Содержимое ссылки неподдерживаемого формата.
	 */
	record UnsupportedLinkContent() implements LinkContent
	{
	}

	/**
	 * Содержимое иерархической ссылки.
	 * @param scheme схема (обязательно).
	 * @param authority основание (обязательно).
	 * @param path путь (обязательно).
	 * @param query запрос (опционально).
	 * @param fragment фрагмент (опционально).
	 */
	record HierarchicalLinkContent( @NotNull String scheme, @NotNull String authority, @NotNull Path path, @Nullable String query, @Nullable String fragment ) implements LinkContent
	{
	}

	/**
	 * Содержимое иерархической ссылки с GitHub.
	 * @param user пользователь.
	 * @param repository репозиторий.
	 */
	record GitHubLinkContent( @NotNull String user, @NotNull String repository ) implements LinkContent
	{
	}

	/**
	 * Содержимое иерархической ссылки со StackOverflow.
	 * @param questionId id вопроса.
	 */
	record StackOverflowLinkContent( @NotNull String questionId ) implements LinkContent
	{
	}
}
