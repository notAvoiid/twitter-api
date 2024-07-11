package com.abreu.spring_security.entities.dto.tweet;

public record FeedItemDTO(
        long id,
        String content,
        String username
) {
}
