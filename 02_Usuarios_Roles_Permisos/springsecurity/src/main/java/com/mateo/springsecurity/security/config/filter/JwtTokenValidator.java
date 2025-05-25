package com.mateo.springsecurity.security.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mateo.springsecurity.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

//Extiende de oncePerRequestFilter que siempre que haya una request, se ejecute el filtro
public class JwtTokenValidator extends OncePerRequestFilter {

    //inyectamos el jwtUtils
    private final JwtUtils jwtUtils;

    //constructor
    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    //Metodo de filtro interno
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        //Si hay token
        if(jwtToken != null){
            //bearer --> eliminamos los primeros 7 chars ("bearer ")
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken); // si sale bien, guardamos el token decodificado

            String username = jwtUtils.extractUsername(decodedJWT); //traer user
            String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString(); //traer claim con permisos separados por ,

            //para ponerlos en el context holder hay que convertirlos a grantedauthority
            //llamamos al authorityutils y traemos los auth separados por , y lo convertimos a granted
            Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities); //separar por coma

            //lo ponemos en el context
            SecurityContext context = SecurityContextHolder.getContext();
            //creamos una instancia de auth pasando la lista de user y permisos
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);

            //Seteamos al contexto la autenticacion
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context); // le pasamos el contexto actualizado
        }
        //si no viene token, va al sig filtro; si no viene arroja error
        filterChain.doFilter(request,response);
    }

}
