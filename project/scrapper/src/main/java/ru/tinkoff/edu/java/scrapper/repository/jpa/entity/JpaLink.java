package ru.tinkoff.edu.java.scrapper.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Entity
@Table( name = "link" )
public class JpaLink
{
	@Id
	String url;
	OffsetDateTime updated;
}
