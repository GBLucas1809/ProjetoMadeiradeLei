package com.madeiradelei.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.madeiradelei.domain.usuario.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // --- GERAR O TOKEN (USADO NO LOGIN) ---
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // Extraímos os papéis (ROLE_CLIENTE, ROLE_GERENTE) para dentro do passaporte
            List<String> perfis = usuario.getPerfis().stream()
                    .map(Enum::name)
                    .toList();

            return JWT.create()
                    .withIssuer("API Madeira de Lei") // Quem emitiu o token
                    .withSubject(usuario.getEmail())  // Quem é o dono do token
                    .withClaim("perfis", perfis)      // Anexamos as permissões (RBAC)
                    .withExpiresAt(gerarDataExpiracao()) // Data de validade
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // --- LER E VALIDAR O TOKEN (USADO NAS REQUISIÇÕES SEGUINTES) ---
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Madeira de Lei")
                    .build()
                    .verify(tokenJWT) // Se a assinatura for falsa ou estiver expirado, lança exceção
                    .getSubject();    // Devolve o e-mail do utilizador
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant gerarDataExpiracao() {
        // Token válido por 2 horas a partir do momento do login
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}