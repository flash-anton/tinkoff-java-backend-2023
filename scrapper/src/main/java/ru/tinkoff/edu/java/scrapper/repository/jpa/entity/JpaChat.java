package ru.tinkoff.edu.java.scrapper.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table( name = "chat" )
public class JpaChat
{
	@Id
	long id;
}
