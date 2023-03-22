package ru.tinkoff.edu.java.scrapper.webclient;

import lombok.NonNull;
import org.springframework.web.reactive.function.client.WebClient;

record WebClientBuilder()
{
	public static WebClient.Builder create( String baseUrl, @NonNull String defaultBaseUrl )
	{
		if( baseUrl == null || !baseUrl.isEmpty() )
		{
			baseUrl = defaultBaseUrl;
		}

		return WebClient.builder().baseUrl( baseUrl );
	}
}
