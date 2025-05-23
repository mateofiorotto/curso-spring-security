package com.mateo.springsecurity.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Permitimos la seguridad x metodos
public class SecurityConfig {

    /*
    * Cadena de filtros
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //Desactivamos csrf porque puede generar incompatibilidad
                .httpBasic(Customizer.withDefaults()) //auth mediante httpbasic, con user y contra, con config customizada y x defecto
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //politicas tenidas en cuenta para crear sesiones, en este caso Stateless (tokens)
        return http.build();
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.GET,"/hola").permitAll() // mediante GET al hola, permite a todos
//                        .requestMatchers(HttpMethod.GET,"/hola-s").hasAuthority("READ") //si queremos acceder a hola-s deben entrar usuarios que puedan leer
//                        .anyRequest().denyAll() //lo otro es denegado
//                );
                //.formLogin(withDefaults());

    }

    //AuthManager: Componente (@bean) que spring crea, configura y ejecuta en toda la app
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        //de la configuracion de autenticacion, accedemos al auth manager y lo devolvemos
        return authenticationConfiguration.getAuthenticationManager();
    }

    //authprovider
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsServiceImp){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();  //Data Access Object. Utiliza el UserDetailsService y PasswordEncoder

        //seteamos el passwordencoder y el userdetailsservice
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImp);

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        //Algoritmo mediante el cual se decodifica, por ahora solo haremos prueba entonces no usaremos algoritmos de encriptados
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        //Usaremos usuarios logicos, porque todavia no hay DB
//
//        //Guardar lista de usuarios
//        List userDetailsList = new ArrayList<>();
//
//        //agregar users. Es una interfaz de SS
//        userDetailsList.add(User.withUsername("mateo")
//                .password("123")
//                .roles("ADMIN") //rol
//                .authorities("CREATE", "READ", "UPDATE", "DELETE") //permisos
//                .build());
//
//        userDetailsList.add(User.withUsername("lucas")
//                .password("1234")
//                .roles("USER") //rol
//                .authorities("READ") //permisos
//                .build());
//
//        userDetailsList.add(User.withUsername("actualizador")
//                .password("12345")
//                .roles("ADMIN") //rol
//                .authorities("UPDATE") //permisos
//                .build());
//
//        //Usuarios logicos en memoria
//        return new InMemoryUserDetailsManager(userDetailsList);
//    }
}
