package ru.tinkoff.edu.java.scrapper.dto;

import lombok.NonNull;

public record GitHubRepositoryInfoResponse( @NonNull String user, @NonNull String repository, @NonNull LinkChanges linkChanges )
{
}
