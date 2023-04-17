package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exception.BadRequestException;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@RestController
@RequiredArgsConstructor
public class ScrapperController
{
	private final ChatService chatService;
	private final LinkService linkService;

	// Зарегистрировать чат
	@PostMapping( "/tg-chat/{id}" )
	public void addChat( @PathVariable long id )
	{
		if( id == 400 )
		{
			throw new BadRequestException( "id == 400" );
			// TODO: /tg-chat/{id} POST responses: Добавить: Чат уже зарегистрирован
		}
		// 200, Чат зарегистрирован
		chatService.add( id );
	}

	// Удалить чат
	@DeleteMapping( "/tg-chat/{id}" )
	public void deleteChat( @PathVariable long id )
	{
		if( id == 400 )
		{
			throw new BadRequestException( "id == 400" );
		}
		// 200, Чат успешно удалён
		chatService.delete( id );
	}

	// Получить все отслеживаемые ссылки
	@GetMapping( "/links" )
	public ListLinksResponse getAllLinks( @RequestHeader( "Tg-Chat-Id" ) long id )
	{
		if( id == 400 )
		{
			throw new BadRequestException( "id == 400" );
			// TODO: /links GET responses: Добавить: Чат не существует
		}
		// 200, Ссылки успешно получены
		LinkResponse[] links = linkService
			.getUrls( id )
			.parallelStream()
			.sorted()
			.map( url -> new LinkResponse( id, url ) )
			.toArray( LinkResponse[]::new );
		return new ListLinksResponse( links, links.length );
	}

	// Добавить отслеживание ссылки
	@PostMapping( "/links" )
	public LinkResponse addLink( @RequestHeader( "Tg-Chat-Id" ) long id, @RequestBody AddLinkRequest req )
	{
		if( id == 400 )
		{
			throw new BadRequestException( "id == 400" );
			// TODO: /links POST responses: Добавить: Чат не существует
			// TODO: /links POST responses: Добавить: Ссылка уже добавлена
		}
		// 200, Ссылка успешно добавлена
		linkService.add( id, req.link() );
		// TODO: /links POST response 200 content: Удалить
		return new LinkResponse( id, req.link() );
	}

	// Убрать отслеживание ссылки
	@DeleteMapping( "/links" )
	public LinkResponse deleteLink( @RequestHeader( "Tg-Chat-Id" ) long id, @RequestBody RemoveLinkRequest req )
	{
		if( id == 400 )
		{
			throw new BadRequestException( "id == 400" );
			// TODO: /links DELETE responses: Добавить: Чат не существует
		}
		// 200, Ссылка успешно убрана
		linkService.delete( id, req.link() );
		// TODO: /links DELETE response 200 content: Удалить
		return new LinkResponse( id, req.link() );
	}
}

