package com.abreu.spring_security.entities.dto.login;

public record LoginResponseDTO (String accessToken, Long expiresIn) {
}
