package io.greenCode.users_management.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtils {
    private SecretKey Key;
    private static final Long EXPIRATION_TYPE = 86400000L;// 24h

    public JWTUtils() {
        String secreteString = "k7T8zL1vP6qW3fQmR9sA2cE5jB0dF8gK4nV2yT7pX1mH5bR8aQ6sL9dY2tW4eJ3kV5qF7uM8oR2zT6aE1xB0pN7hD3gS9jF8kL4mV7tQ2rC5hA0bY6jN1vG3sK8wD5uZ0fR2eX8qJ1lM7hY4oT9zB2nV5cS6jF1wG8rL2xN0dQ4pY3mE6kB1tW8jA9gH5sF2rX7cM4vP0yL1dZ6qT8jB2gW5kR3fE9nD7vY2sX4mH1qL8pT5wN0aB3jF6gK9eR1cV8yS4mD2bH7lT5qG1zW6nJ0pF3eY9dK2tA4rM5wQ7sL1vN6xD3jB8fG0kH4qT2mE9nR1yP8wC5gL3xB7jF0dK2vM6sP9tJ4qW1eY8fA3nR5dX2mH0lT";
        // chuyen code tu string sang byte
        byte[] keyByte = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        // ma hoa byte thong qua algrithm hmacSHA256
        this.Key = new SecretKeySpec(keyByte, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails) {
        // builder la bat dau tao the

        return Jwts.builder()
                // ghi ten len cai the ra vao
                .subject(userDetails.getUsername())
                // tao cai the nay luc nao
                .issuedAt(new Date(System.currentTimeMillis()))
                // ngay het han
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TYPE))
                // ki ten vao cai the ra vao nay
                .signWith(Key)
                // conmpact la hoan tat
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TYPE))
                .signWith(Key)
                .compact();
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
