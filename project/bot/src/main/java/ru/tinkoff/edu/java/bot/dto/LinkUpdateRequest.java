package ru.tinkoff.edu.java.bot.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class LinkUpdateRequest
{
	private long id;
	private @NonNull String url;
	private @NonNull String description;
	private long[] tgChatIds;
}
