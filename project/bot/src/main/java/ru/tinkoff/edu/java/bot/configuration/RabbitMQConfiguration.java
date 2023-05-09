package ru.tinkoff.edu.java.bot.configuration;

import lombok.Data;
import lombok.NonNull;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

import java.util.HashMap;

/**
 * Конфигурация MQ.<br>
 * Если объявленных бинами Exchange, Queue и Binding нет на AMQP-сервере, то Spring создаст их автоматически.
 */
@Configuration
@ConfigurationProperties( prefix = "rabbitmq", ignoreUnknownFields = false )
@Data
public class RabbitMQConfiguration
{
	private String queueName;
	private String exchangeName;

	@Bean
	public @NonNull DirectExchange messageExchange()
	{
		return new DirectExchange( exchangeName );
	}

	@Bean
	public @NonNull Queue messageQueue()
	{
		return QueueBuilder
			.durable( queueName )
			.withArgument( "x-dead-letter-exchange", exchangeName + ".dlx" )
			.build();
	}

	@Bean
	public @NonNull Binding messageBinding()
	{
		return BindingBuilder.bind( messageQueue() ).to( messageExchange() ).with( "routingKey" );
	}


	@Bean
	public @NonNull FanoutExchange deadLetterExchange()
	{
		return new FanoutExchange( exchangeName + ".dlx" );
	}

	@Bean
	public @NonNull Queue deadLetterQueue()
	{
		return new Queue( queueName + ".dlq" );
	}

	@Bean
	public @NonNull Binding deadLetterBinding()
	{
		return BindingBuilder.bind( deadLetterQueue() ).to( deadLetterExchange() );
	}


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
