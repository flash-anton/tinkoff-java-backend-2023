package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.botclient.dto.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer
{
	private final RabbitTemplate rabbitTemplate;
	private final Binding messageBinding;

	public void send( LinkUpdateRequest update )
	{
		rabbitTemplate.convertAndSend( messageBinding.getExchange(), messageBinding.getRoutingKey(), update );
	}
}
