package ru.tinkoff.edu.java.scrapper.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table( name = "chat_link" )
public class JpaChatLink
{
	@Id
	long chat_id;
	@Id
	String link_url;
}
