package com.mateo.springsecurity.dto;

import jakarta.validation.constraints.NotBlank;

//Records: proporciona ciertas funcionalidades basicas usadas para datos, en DTO generalmente
public record AuthLoginRequestDTO (@NotBlank String username,
                                   @NotBlank String password){
}
