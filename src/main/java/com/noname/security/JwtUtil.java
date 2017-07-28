package com.noname.security;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtil {
    public static String getIdFromToken(String jws) {
        return (String) Jwts.parser()
                .setSigningKey(SecurityConfig.KEY)
                .parseClaimsJws(jws).getBody().get("id");
    }
    public static String generateToken(String id) {
        return Jwts.builder()
                .claim("id", id)
                .setExpiration(new Date(System.currentTimeMillis() + 604800))
                .signWith(SignatureAlgorithm.HS256, SecurityConfig.KEY)
                .compact();
    }
    public static boolean validateToken(String jws) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityConfig.KEY)
                    .parseClaimsJws(jws);
            System.out.println(System.currentTimeMillis());
            System.out.println(claimsJws.getBody().getExpiration().getTime());
            if (System.currentTimeMillis() > claimsJws.getBody().getExpiration().getTime()) {
                return false;
            }
        }catch (JwtException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
