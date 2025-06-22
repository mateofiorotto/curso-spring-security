package com.mateo.oauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()")
public class GreetingsController {

    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello(){
        return "Hello - No security";
    }

    @GetMapping("/hellosec")
    @PreAuthorize("isAuthenticated()")
    public String helloSecurity(){
        return "Hello - with security";
    }
}
