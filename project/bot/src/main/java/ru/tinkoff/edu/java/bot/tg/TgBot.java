package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TgBot implements AutoCloseable
{
	private final ApplicationConfig config;
	private final List<TgCommand> commands;
	private TelegramBot bot;

	public TgBot( ApplicationConfig config )
	{
		this.config = config;

		TgCommandHelp tgCommandHelp = new TgCommandHelp();

		commands = List.of(
			new TgCommandStart( config ),
			tgCommandHelp,
			new TgCommandTrack( config ),
			new TgCommandUntrack( config ),
			new TgCommandList( config )
		);

		tgCommandHelp.setHelp( buildHelp( commands ) );
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
		return command.process( update );
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
		System.out.printf( "%s  TG_BOT  chatId=%d  updateId=%d  text=%s\n",
			OffsetDateTime.now(), chatId, updateId, text );
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
