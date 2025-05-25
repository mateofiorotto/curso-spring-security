package com.mateo.springsecurity.dto;

//deserializar y serializar usando jackson: convierte obj java a json y viceversa

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//Lo muestra en orden en el json
@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponseDTO (String username,
                               String message,
                               String jwt,
                               boolean status){
}
