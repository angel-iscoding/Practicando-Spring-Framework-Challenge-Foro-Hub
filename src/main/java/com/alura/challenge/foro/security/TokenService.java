package com.alura.challenge.foro.security;

import com.alura.challenge.foro.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String apiSecret;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            Instant fechaExpiracion = generarFechaExpiracion();
            
            // Log para debugging
            System.out.println("🟢 Generando token JWT:");
            System.out.println("   Usuario: " + usuario.getLogin());
            System.out.println("   Fecha actual: " + Instant.now());
            System.out.println("   Fecha expiración: " + fechaExpiracion);
            System.out.println("   Duración configurada: " + expiration + " ms (" + (expiration/1000/60) + " minutos)");
            
            return JWT.create()
                    .withIssuer("foro-hub")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(fechaExpiracion)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("foro-hub")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            // Proporcionar más información sobre el tipo de error
            if (exception.getMessage().contains("expired")) {
                throw new RuntimeException("Token JWT expirado", exception);
            } else {
                throw new RuntimeException("Token JWT inválido", exception);
            }
        }
    }

    private Instant generarFechaExpiracion() {
        // Usar la configuración de expiración en milisegundos
        return Instant.now().plusMillis(expiration);
    }
}
