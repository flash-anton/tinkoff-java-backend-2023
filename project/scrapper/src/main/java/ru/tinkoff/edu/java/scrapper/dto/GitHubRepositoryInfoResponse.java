package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;
import java.time.OffsetDateTime;

public record GitHubRepositoryInfoResponse( @NonNull String full_name, @NonNull OffsetDateTime updated_at, @NonNull OffsetDateTime pushed_at )
{
}
