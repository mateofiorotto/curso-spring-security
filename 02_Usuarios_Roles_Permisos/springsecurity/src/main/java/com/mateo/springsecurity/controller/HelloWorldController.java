package com.mateo.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()") //denegamos la entrada a endpoints que no tengan especificada la restriccion de sugirdad
public class HelloWorldController {

    @GetMapping("hola-s")
    @PreAuthorize("hasRole('ADMIN')") //Si el usuario auth tiene permisos para leer, puede acceder
    public String secHelloWorld(){
        return "Hola mundo con seguridad";
    }

    @GetMapping("hola")
    @PreAuthorize("permitAll()") //Cualquiera puede acceder
    public String helloWorld(){
        return "Hola mundo sin seguridad";
    }

    //Como no esta configurado no se puede acceder ya que por defecto toma el pre authorize del restcontroller, que deniega todo
    @GetMapping("hola2")
    public String helloWorld2(){
        return "Hola mundo 2 sin seguridad";
    }
}
