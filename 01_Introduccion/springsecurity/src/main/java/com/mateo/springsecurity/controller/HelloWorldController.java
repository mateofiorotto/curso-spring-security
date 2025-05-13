package com.mateo.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("hola-s")
    public String secHelloWorld(){
        return "Hola mundo con seguridad";
    }

    @GetMapping("hola")
    public String helloWorld(){
        return "Hola mundo sin seguridad";
    }
}
