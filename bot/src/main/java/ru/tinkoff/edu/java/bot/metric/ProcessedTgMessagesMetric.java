package ru.tinkoff.edu.java.bot.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

public class ProcessedTgMessagesMetric
{
	public static final Counter COUNTER = Metrics.counter( "processed_tg_messages_counter" );
}
