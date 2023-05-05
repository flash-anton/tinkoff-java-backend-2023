package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.botclient.dto.LinkUpdateRequest;

@Service
@RequiredArgsConstructor
@RabbitListener( queues = "${rabbitmq.queueName}" + ".dlq" )
public class ScrapperDLQListener
{
	private final Logger logger = LogManager.getLogger();

	@RabbitHandler
	public void receiver( LinkUpdateRequest req )
	{
		logger.error( "DLQ: " + req );
	}
}
