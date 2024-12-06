package com.example.demo.filters.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtUtility {
    private final String SECRET = "e6144fe98e0adf59f924dbeb2f9da6d8016c5a7ad729e561dbd0f514bb576f0f";
    private Key getSigninKey() {
        byte[] decodedBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(decodedBytes);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigninKey()).build().parseSignedClaims(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, Map<String, Object> user) {
        return extractUserName(token).equals(user.get("username").toString()) && !hasTokenExpired(token);
    }

    public boolean hasTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(Map<String, Object> user, Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .setSubject(user.get("username").toString())
                .claims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000000*87))
                .compact();
    }
}
