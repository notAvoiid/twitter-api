package com.abreu.spring_security.entities.dto;

public record LoginResponseDTO (String accessToken, Long expiresIn) {
}
