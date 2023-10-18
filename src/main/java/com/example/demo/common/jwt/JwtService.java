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


    public String getPayLoadByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);
    }

    public String createAccessTokenByServiceId(String serviceId) {
        return createToken(serviceId, accessTokenValidityMilliSeconds);
    }

    public String createAccessTokenByRefreshToken(String refreshToken) {
        String serviceId = getPayLoadByToken(refreshToken);
        return createToken(serviceId, accessTokenValidityMilliSeconds);
    }

    public String createRefreshToken(String serviceId) {
        return createToken(serviceId, refreshTokenValidityMilliSeconds);
    }

    private String createToken(String serviceId, long tokenValidityMilliSeconds) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + tokenValidityMilliSeconds);

        return Jwts.builder()
                .setSubject(serviceId)
                .claim("serviceId", serviceId)
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