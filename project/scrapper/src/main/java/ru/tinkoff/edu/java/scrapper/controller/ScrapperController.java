package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exception.BadRequestException;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;

@RestController
public class ScrapperController
{
	// Зарегистрировать чат
	@PostMapping( "/tg-chat/{id}" )
	public void addChat( @PathVariable long id )
	{
		switch( (int)id )
		{
			case 400 -> throw new BadRequestException( "id == 400" );
			// TODO: /tg-chat/{id} POST responses: Добавить: Чат уже зарегистрирован
		}
		// 200, Чат зарегистрирован
	}

	// Удалить чат
	@DeleteMapping( "/tg-chat/{id}" )
	public void deleteChat( @PathVariable long id )
	{
		switch( (int)id )
		{
			case 400 -> throw new BadRequestException( "id == 400" );
			case 404 -> throw new NotFoundException( "Чат не существует", "id == 404" );
		}
		// 200, Чат успешно удалён
	}

	// Получить все отслеживаемые ссылки
	@GetMapping( "/links" )
	public ListLinksResponse getAllLinks( @RequestHeader( "Tg-Chat-Id" ) long id )
	{
		return switch( (int)id )
		{
			case 400 -> throw new BadRequestException( "id == 400" );
			// TODO: /links GET responses: Добавить: Чат не существует
			default -> new ListLinksResponse( new LinkResponse[0], 0 ); // 200, Ссылки успешно получены
		};
	}

	// Добавить отслеживание ссылки
	@PostMapping( "/links" )
	public LinkResponse addLink( @RequestHeader( "Tg-Chat-Id" ) long id, @RequestBody AddLinkRequest req )
	{
		return switch( (int)id )
		{
			case 400 -> throw new BadRequestException( "id == 400" );
			// TODO: /links POST responses: Добавить: Чат не существует
			// TODO: /links POST responses: Добавить: Ссылка уже добавлена
			// TODO: /links POST response 200 content: Удалить
			default -> new LinkResponse( id, req.link() ); // 200, Ссылка успешно добавлена
		};
	}

	// Убрать отслеживание ссылки
	@DeleteMapping( "/links" )
	public LinkResponse deleteLink( @RequestHeader( "Tg-Chat-Id" ) long id, @RequestBody RemoveLinkRequest req )
	{
		return switch( (int)id )
		{
			case 400 -> throw new BadRequestException( "id == 400" );
			case 404 -> throw new NotFoundException( "Ссылка не найдена", "id == 404" );
			// TODO: /links DELETE responses: Добавить: Чат не существует
			// TODO: /links DELETE response 200 content: Удалить
			default -> new LinkResponse( id, req.link() ); // 200, Ссылка успешно убрана
		};
	}
}

