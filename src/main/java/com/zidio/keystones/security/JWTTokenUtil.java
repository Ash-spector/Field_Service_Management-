package com.zidio.keystones.security;

import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.enums.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JWTTokenUtil {

    private final Key key;

    private final long validToken = 12 * 60 * 60 * 1000L;

    public JWTTokenUtil() {

        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.isEmpty()) {
            secret = "MyVerySecretKeyForJWTAuthentication123456789";
        }

        key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(UserAuth user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", user.getRole());

        Set<Permission> permissions =
                RoleBasedPermission.getRoleBasedPermission()
                        .get(user.getRole());

        if (permissions != null) {
            claims.put("permissions", permissions);
        }

        Date now = new Date();

        Date expire = new Date(now.getTime() + validToken);

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expire)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {

        try {

            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailFromToken(String token) {

        return getClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getPermissionsFromToken(String token) {

        return Set.copyOf(
                getClaims(token)
                        .get("permissions", Set.class)
        );
    }

    public String exractToken(String header) {

        if (header != null && header.startsWith("Bearer ")) {

            return header.substring(7);
        }

        return null;
    }
}