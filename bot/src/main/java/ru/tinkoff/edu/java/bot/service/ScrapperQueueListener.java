package ru.tinkoff.edu.java.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@RabbitListener( queues = "${rabbitmq.queueName}" )
public class ScrapperQueueListener
{
	private final TgChatsNotifier tgChatsNotifier;

	@RabbitHandler
	public void receiver( LinkUpdateRequest req )
	{
		tgChatsNotifier.linkUpdate( req.getDescription(), req.getUrl(), req.getTgChatIds() );
	}
}
