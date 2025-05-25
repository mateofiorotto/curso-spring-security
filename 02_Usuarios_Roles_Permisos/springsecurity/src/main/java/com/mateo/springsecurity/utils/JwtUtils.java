package com.mateo.springsecurity.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//Indicamos que es un componente de Spring, esta clase se carga automaticamente dentro del contexto de la app
//ya que el primer paso es la auth
@Component
public class JwtUtils {

    //Traer de la app.properties, para cuando hagamos la validacion se comparen y que verificar que los tokens no sean manipulados
    //es decir, que el token sea autentico
    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    //CREACION de tokens: importamos el Authentication para guardar el token y uso de otros metodos
    public String createToken(Authentication authentication) {

        //USAREMOS hmac
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        //getPrincipal metodo exclusivo de authentication. Nos devuelve el usuario autenticado (el nombre del usuario principal en la auth)
        //queda en el contextHolder
        String username = authentication.getPrincipal().toString();

        //obtener permisos
        //Los permisos se separan por ",". Obtenemos los authorities que son una collection, lo transformamos a stream, lo
        //mapeamos para traer cada authority y los juntamos con collect pero separados por ","
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator) //quien es el user que genera el token (el encargado)
                .withSubject(username) //es el sujeto que traigo como usuario en el authentication (dentro de los claims)
                .withClaim("authorities", authorities) //lista de permisos
                .withIssuedAt(new Date()) //cuando se creo el token
                .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60000))) //cuando expira el token (en este caso media hora, 10 min o 15 para pags sensibles)
                .withJWTId(UUID.randomUUID().toString()) //id del token
                .withNotBefore(new Date(System.currentTimeMillis())) //a partir de cuando es valido el token
                .sign(algorithm); //firmamos mediante el algoritmo

        return jwtToken;
    }

    //DECODIFICAR TOKEN: interfaz DecodedJWT. Usa el mismo algoritmo para desencriptarlo.
    public DecodedJWT validateToken(String token) {
        try {
            //verficador construido por el hmac256
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            JWTVerifier verifier = JWT.require(algorithm) //requiere algoritmo para hacer la comparacion
                    .withIssuer(this.userGenerator) //quien es el generador del verificador
                    .build(); //construimos el verificador

            //si esta ok devuelve el jwt decodificado y verificado
            DecodedJWT decodedJWT = verifier.verify(token); //verificamos el token

            return decodedJWT;

        } catch (JWTVerificationException e) {

            throw new JWTVerificationException("Token no valido. No autorizado");

        }
    }

    //METODO para obtener el usuario del token
    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString(); //el sujeto es el username
    }

    //OBTENER UN CLAIM en particular
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName); //obtenemos el claim
    }

    //OBTENER CLAIMS del token (parte de la info guardada, cada atributo es un claim)
    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT) {
            return decodedJWT.getClaims(); //lista de claims
    }

}