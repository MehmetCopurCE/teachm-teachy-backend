package com.project.teachmteachybackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${project.app.secret}")
    private String APP_SECRET;

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    public Boolean validateToken(String token, JwtUserDetails userDetails) {
//        String username = extractUser(token);
//        Date expirationDate = extractExpiration(token);
//        return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .setAllowedClockSkewSeconds(5*60)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    private Date extractExpiration(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
    public String extractUser(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(APP_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
