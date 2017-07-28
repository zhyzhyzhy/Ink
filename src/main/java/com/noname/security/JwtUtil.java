package com.noname.security;

import io.jsonwebtoken.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    public static User getUserFromToken(String jws) {
        User user = new User();
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(SecurityConfig.KEY)
                .parseClaimsJws(jws);

        user.setUserName((String) claimsJws.getBody().get("sub"));
        user.setRoles((List<String>) claimsJws.getBody().get("roles"));
        return user;
    }
    public static String generateToken(User user) {
        return Jwts.builder()
                .claim("sub", user.getUserName())
                .claim("roles", user.getRoles())
                .setExpiration(new Date(System.currentTimeMillis() + 604800))
                .signWith(SignatureAlgorithm.HS256, SecurityConfig.KEY)
                .compact();
    }
    public static boolean validateToken(String jws) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityConfig.KEY)
                    .parseClaimsJws(jws);
//            System.out.println(System.currentTimeMillis());
//            System.out.println(claimsJws.getBody().getExpiration().getTime());
        } catch (JwtException | NullPointerException e) {
            e.printStackTrace();
            //error or just out of time
            return false;
        }
        return true;
    }
}
