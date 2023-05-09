package ru.tinkoff.edu.java.scrapper.botclient.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.net.URI;

@Getter
@Setter
@ToString
public class LinkUpdateRequest
{
	private long id;
	private @NonNull URI url;
	private @NonNull String description;
	private long[] tgChatIds;
}
