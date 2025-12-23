package com.schedula.schedula.config.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${security.jwt.secret-key}")
    private String secretkey;
    @Value("${app.jwt.expiration-in-ms}")
    private String jwtExpirationInMs;

    @Value("${app.jwt.remember-me-expiration-in-ms}")
    private String jwtRememberMeExpirationInMs;

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()
                + (rememberMe ? Long.parseLong(jwtRememberMeExpirationInMs) : Long.parseLong(jwtExpirationInMs)));
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .and()
                .signWith(getKey())
                .compact();
        /*
         * Map<String, Object> claims = new HashMap<>();
         * return Jwts.builder()
         * .setClaims(claims)
         * .setSubject(userDetails.getUsername())
         * 
         * .signWith(SignatureAlgorithm.HS512, secretkey)
         * .compact();
         */
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUserName(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // التحقق من صحة Token
    /*public String checkToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            // التحقق من انتهاء الصلاحية
            if (claims.getBody().getExpiration().before(new Date())) {
                return null;
            }

            return claims.getBody().getSubject(); // إرجاع اسم المستخدم
        } catch (SignatureException ex) {
            // توقيع غير صالح
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // Token غير صالح
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Token منتهي الصلاحية
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Token غير مدعوم
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // Token فارغ
            System.err.println("JWT claims string is empty");
        }
        return null;
    }*/

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}