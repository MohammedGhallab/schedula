package com.schedula.schedula.config.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.schedula.schedula.user.CustomUserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTService {
    // 1. الثوابت في أعلى الكلاس
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_FULL_NAME = "fullName";
    public static final String CLAIM_IS_ACTIVE = "isActive";

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

    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(CLAIM_USER_ID, userDetails.getId().toString());
        extraClaims.put(CLAIM_ROLE, userDetails.getRole());
        extraClaims.put(CLAIM_FULL_NAME, userDetails.getName());
        extraClaims.put(CLAIM_IS_ACTIVE, userDetails.isEnabled());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 ساعة
                .signWith(getKey())
                .compact();
    }
    /*
     * public String generateToken(String email, boolean rememberMe) {
     * Map<String, Object> claims = new HashMap<>();
     * Date now = new Date();
     * Date expiryDate = new Date(now.getTime()
     * + (rememberMe ? Long.parseLong(jwtRememberMeExpirationInMs) :
     * Long.parseLong(jwtExpirationInMs)));
     * return Jwts.builder()
     * .claims()
     * .add(claims)
     * .subject(email)
     * .issuedAt(now)
     * .expiration(expiryDate)
     * .and()
     * .signWith(getKey())
     * .compact();
     * 
     * Map<String, Object> claims = new HashMap<>();
     * return Jwts.builder()
     * .setClaims(claims)
     * .setSubject(userDetails.getUsername())
     * 
     * .signWith(SignatureAlgorithm.HS512, secretkey)
     * .compact();
     * 
     * }
     */

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

    // 1. استخراج الـ ID وتحويله إلى UUID
    public UUID extractUserId(String token) {
        String userId = extractClaim(token, claims -> claims.get(CLAIM_USER_ID, String.class));
        return (userId != null) ? UUID.fromString(userId) : null;
    }

    // 2. استخراج الدور (Role)
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_ROLE, String.class));
    }

    // 3. استخراج الاسم الكامل
    public String extractFullName(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_FULL_NAME, String.class));
    }

    // 4. استخراج حالة الحساب
    public Boolean extractIsActive(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_IS_ACTIVE, Boolean.class));
    }

    // 5. التحقق من صحة التوكن (بدون الحاجة لمقارنة بالـ DB)
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }
    // التحقق من صحة Token
    /*
     * public String checkToken(String token) {
     * try {
     * Jws<Claims> claims = Jwts.parser()
     * .setSigningKey(getKey())
     * .build()
     * .parseClaimsJws(token);
     * 
     * // التحقق من انتهاء الصلاحية
     * if (claims.getBody().getExpiration().before(new Date())) {
     * return null;
     * }
     * 
     * return claims.getBody().getSubject(); // إرجاع اسم المستخدم
     * } catch (SignatureException ex) {
     * // توقيع غير صالح
     * System.err.println("Invalid JWT signature");
     * } catch (MalformedJwtException ex) {
     * // Token غير صالح
     * System.err.println("Invalid JWT token");
     * } catch (ExpiredJwtException ex) {
     * // Token منتهي الصلاحية
     * System.err.println("Expired JWT token");
     * } catch (UnsupportedJwtException ex) {
     * // Token غير مدعوم
     * System.err.println("Unsupported JWT token");
     * } catch (IllegalArgumentException ex) {
     * // Token فارغ
     * System.err.println("JWT claims string is empty");
     * }
     * return null;
     * }
     */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}