package com.abreu.spring_security.entities.dto.tweet;

import java.util.List;

public record FeedDTO(
        List<FeedItemDTO> feedItems,
        int page,
        int pageSize,
        int totalPages,
        long totalElements
) {
}
