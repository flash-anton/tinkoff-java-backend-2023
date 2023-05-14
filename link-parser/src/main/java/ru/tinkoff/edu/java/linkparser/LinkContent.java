package ru.tinkoff.edu.java.linkparser;

import org.jetbrains.annotations.NotNull;

/**
 * Содержимое ссылки.
 */
public sealed interface LinkContent permits LinkContent.GitHubLinkContent, LinkContent.StackOverflowLinkContent, LinkContent.UnsupportedLinkContent
{
	/**
	 * Содержимое ссылки неподдерживаемого формата.
	 */
	record UnsupportedLinkContent() implements LinkContent
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
