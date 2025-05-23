package org.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EjemploUsoApplication {
    public static void main(String[] args) {

        //construimos extra claims: anade info adicional al token, agregando una claim con clave name y valor prueba jwt
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", "prueba jwt");

        //construimos jwt: con fechas de emision y expiracion. Expira 1 min despues
        Date issuedAt = new Date (System.currentTimeMillis());
        Date expiration = new Date (issuedAt.getTime() + (1 *60 * 1000));

        //construimos el jwt usando el builder jwts.builder(
        String jwt;
        jwt = Jwts.builder()

                //cabecera, especifica el tipo JWT
                .header()
                .type("JWT")
                .and()

                //payload, contenido del token: sujeto, fecha expiracion, emision y reclamos adicionales
                .subject("pruebajwt")
                .expiration(expiration)
                .issuedAt(issuedAt)
                .claims(extraClaims)

                //firma: usando llave generada por generateKey
                //usa el algoritmo hmac-sha256
                .signWith(generateKey(), Jwts.SIG.HS256)

                //compacta el jwt
                .compact();

        //vemos nuestro jwt generado por pantalla
        System.out.println(jwt);


    }

    public static SecretKey generateKey () {
        //tiene que ser larga porque dijimos que cumple con HS256
        String secretKey = "esta es mi key super segura 12345688789 HOLA que tal";

        return Keys.hmacShaKeyFor(secretKey.getBytes());


    }
}