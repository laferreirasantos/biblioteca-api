package com.pge.biblioteca.security.jwt;

import com.pge.biblioteca.security.model.UsuarioSecurity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration; // em milissegundos

    public String gerarToken(UsuarioSecurity usuarioSecurity) {
        return Jwts.builder()
                .setSubject(usuarioSecurity.getUsername())
                .claim("role", usuarioSecurity.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getAssinatura(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameDoToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();
            return expirationDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAssinatura())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getAssinatura() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
