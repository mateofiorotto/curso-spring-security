package com.mateo.springsecurity.controller;

import com.mateo.springsecurity.dto.AuthLoginRequestDTO;
import com.mateo.springsecurity.dto.AuthResponseDTO;
import com.mateo.springsecurity.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") //todo lo que tiene que ver con auth se suele poner en /auth por estandar
public class AuthenticationController {
    private final UserDetailsServiceImp userDetailsServiceImp;

    public AuthenticationController(UserDetailsServiceImp userDetailsServiceImp) {
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest){

        return new ResponseEntity<>(this.userDetailsServiceImp.loginUser(userRequest), HttpStatus.OK);
    }
}
