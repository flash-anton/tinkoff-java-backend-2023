package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Data;
import lombok.NonNull;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "rabbitmq", ignoreUnknownFields = false )
@Data
public class RabbitMQConfiguration
{
	private String queueName;
	private String exchangeName;

	@Bean
	public @NonNull DirectExchange directExchange()
	{
		return new DirectExchange( exchangeName );
	}

	@Bean
	public @NonNull Queue getQueue()
	{
		return new Queue( queueName );
	}

	@Bean
	public @NonNull Binding binding( @NonNull Queue queue, @NonNull DirectExchange directExchange )
	{
		return BindingBuilder.bind( queue ).to( directExchange ).with( "routingKey" );
	}

	@Bean
	public @NonNull MessageConverter jsonMessageConverter()
	{
		return new Jackson2JsonMessageConverter();
	}
}
