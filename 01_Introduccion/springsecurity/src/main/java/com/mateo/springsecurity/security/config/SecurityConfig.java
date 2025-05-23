package com.mateo.springsecurity.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Filtros de seguridad: cadena de filtros
    @Bean //indica que la cadena trabaja como un bean, un objeto que obtiene los detalles de configuracion de la cadena
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests() //que se autoriza y no
                    .requestMatchers("/hola").permitAll() //regla de autorizacion, permitir entrar sin autenticacion
                .anyRequest().authenticated() //cualquier solicitud que no coincida con esta ruta, exige auth
                .and()
                .formLogin().permitAll() //que aparezca el login o que cualquiera pueda acceder a login
                .and()
                .httpBasic() //esquema de autenticacion basico, en cada solicitud se autentica (no es seguro)
                .and()
                .build(); //construir cadena de filtros
    }
}
