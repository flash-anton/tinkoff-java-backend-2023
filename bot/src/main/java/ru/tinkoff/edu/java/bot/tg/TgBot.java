package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.metric.ProcessedTgMessagesMetric;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TgBot implements AutoCloseable
{
	private final ApplicationConfig config;
	private final List<TgCommand> commands;
	private final Logger logger = LogManager.getLogger();
	private TelegramBot bot;

	public TgBot( ApplicationConfig config, List<TgCommand> commands )
	{
		this.config = config;
		this.commands = commands;

		commands.parallelStream()
				.filter( TgCommandHelp.class::isInstance )
				.map( TgCommandHelp.class::cast )
				.findAny()
				.ifPresent( cmd -> cmd.setHelp( buildHelp( commands ) ) );
	}

	public void start()
	{
		if( bot == null )
		{
			bot = new TelegramBot( config.telegramToken() );
			bot.execute( new SetMyCommands( buildMyCommands( commands ) ) );
			bot.setUpdatesListener( updates ->
			{
				updates
					.stream()
					.peek( this::printUpdate )
					.map( this::processUpdate )
					.forEach( this::sendMessage );
				return UpdatesListener.CONFIRMED_UPDATES_ALL;
			});
		}
	}

	public void stop()
	{
		if( bot != null )
		{
			bot.removeGetUpdatesListener();
		}
	}

	@Override
	public void close()
	{
		stop();
	}

	public SendMessage processUpdate( Update update )
	{
		long chatId = update.message().chat().id();

		TgCommand command = findCommand( update );
		if( command == null )
		{
			return new SendMessage( chatId, "Команда не поддерживается" );
		}

		try
		{
			return command.process( update );
		}
		catch( RuntimeException ex )
		{
			logger.error( ex.getMessage() );
			return new SendMessage( chatId, "Ошибка" );
		}
		finally
		{
			ProcessedTgMessagesMetric.COUNTER.increment();
		}
	}

	public void sendMessage( SendMessage message )
	{
		if( message != null )
		{
			bot.execute( message );
		}
	}

	private static String buildHelp( List<TgCommand> commands )
	{
		return commands
			.stream()
			.map( c -> c.name() + " -- " + c.description() )
			.collect( Collectors.joining( "\n", "Доступны команды:\n", "" ));
	}

	private static BotCommand[] buildMyCommands( List<TgCommand> commands )
	{
		return commands
			.stream()
			.map( TgCommand::toBotCommand )
			.toArray( BotCommand[]::new );
	}

	private void printUpdate( Update update )
	{
		int updateId = update.updateId();
		long chatId = update.message().chat().id();
		String text = update.message().text();
		logger.info( "updateId={} chatId={} text={}", updateId, chatId, text );
	}

	private TgCommand findCommand( Update update )
	{
		return commands
			.parallelStream()
			.filter( cmd -> cmd.isSupported( update ) )
			.findAny()
			.orElse( null );
	}
}
