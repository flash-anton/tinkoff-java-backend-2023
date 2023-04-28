package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

import java.util.HashMap;

@Configuration
public class RabbitMQConfiguration
{
	@Bean
	public MessageConverter jsonMessageConverter()
	{
		HashMap<String, Class<?>> mappings = new HashMap<>();
		mappings.put( "ru.tinkoff.edu.java.scrapper.botclient.dto.LinkUpdateRequest", LinkUpdateRequest.class );

		DefaultClassMapper classMapper = new DefaultClassMapper();
		classMapper.setTrustedPackages( "ru.tinkoff.edu.java.scrapper.botclient.dto.*" );
		classMapper.setIdClassMapping( mappings );

		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		jsonConverter.setClassMapper( classMapper );
		return jsonConverter;
	}
}
