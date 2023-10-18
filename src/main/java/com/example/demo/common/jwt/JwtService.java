package com.example.demo.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token-validity-milli-seconds.access}")
    private Long accessTokenValidityMilliSeconds;
    @Value("${jwt.token-validity-milli-seconds.refresh}")
    private Long refreshTokenValidityMilliSeconds;

    private byte[] keyBytes;
    private SecretKey key;

    @PostConstruct
    void afterBeanInit() {
        keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    public Long getPayLoadByToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("id", Long.class);
        } catch (
                SecurityException |
                ExpiredJwtException |
                MalformedJwtException |
                UnsupportedJwtException |
                IllegalArgumentException e
        ) {
            throw new AuthenticationTokenException(e.getMessage());
        }
    }

    public String createAccessTokenById(Long id) {
        return createToken(id, accessTokenValidityMilliSeconds);
    }
    public String createRefreshTokenById(Long id) {
        return createToken(id, refreshTokenValidityMilliSeconds);
    }

    public String createAccessTokenByRefreshToken(String refreshToken) {
        Long id = getPayLoadByToken(refreshToken);
        return createToken(id, accessTokenValidityMilliSeconds);
    }

    private String createToken(Long id, long tokenValidityMilliSeconds) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + tokenValidityMilliSeconds);

        return Jwts.builder()
                .claim("id", id)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            // Handle security exception
        } catch (ExpiredJwtException e) {
            // Handle expired token
        } catch (MalformedJwtException e) {
            // Handle malformed token
        } catch (UnsupportedJwtException e) {
            // Handle unsupported token
        } catch (IllegalArgumentException e) {
            // Handle illegal argument
        }
        return false;
    }
}