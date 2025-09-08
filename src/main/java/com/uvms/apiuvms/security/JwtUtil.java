package com.uvms.apiuvms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility class for token generation, validation and information extraction
 * Supports both Vendor and Admin JWTs
 */
@Component
public class JwtUtil {

    // JWT Configuration
    @Value("${jwt.secret:uvmsSecretKeyForJWTTokenGenerationThatIsAtLeast64BytesLong!2025}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600}") // expiration in seconds (default 1 hour)
    private Long jwtExpirationInSeconds;

    // ---------------------- GENERATE TOKENS ----------------------

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(UserDetails userDetails, com.uvms.apiuvms.entity.Vendors vendor) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("vendor_id", vendor.getVendorId());
        claims.put("first_name", vendor.getFirstName());
        claims.put("last_name", vendor.getLastName());
        claims.put("phone_number", vendor.getPhoneNumber());
        claims.put("company_name", vendor.getCompanyName());
        claims.put("tin_number", vendor.getTinNumber());
        claims.put("business_address", vendor.getBusinessAddress());
        claims.put("business_type", vendor.getBusinessType());
        claims.put("is_active", vendor.isActive());
        claims.put("role", "VENDOR");
        return createToken(claims, userDetails.getUsername());
    }

    public String generateToken(UserDetails userDetails, com.uvms.apiuvms.entity.Admins admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin_id", admin.getAdmin_id());
        claims.put("name", admin.getName());
        claims.put("email", admin.getEmail());
        claims.put("role", admin.getRole().name());
        claims.put("is_active", admin.isIs_active());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // ---------------------- EXTRACT CLAIMS ----------------------

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractVendorId(String token) {
        return extractClaim(token, claims -> claims.get("vendor_id", Integer.class));
    }

    public String extractFirstName(String token) {
        return extractClaim(token, claims -> claims.get("first_name", String.class));
    }

    public String extractLastName(String token) {
        return extractClaim(token, claims -> claims.get("last_name", String.class));
    }

    public String extractCompanyName(String token) {
        return extractClaim(token, claims -> claims.get("company_name", String.class));
    }

    public Boolean extractVendorIsActive(String token) {
        return extractClaim(token, claims -> claims.get("is_active", Boolean.class));
    }

    public Integer extractAdminId(String token) {
        return extractClaim(token, claims -> claims.get("admin_id", Integer.class));
    }

    public String extractAdminName(String token) {
        return extractClaim(token, claims -> claims.get("name", String.class));
    }

    public String extractAdminRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Boolean extractAdminIsActive(String token) {
        return extractClaim(token, claims -> claims.get("is_active", Boolean.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ---------------------- VALIDATION ----------------------

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    // ---------------------- UTIL ----------------------

    public Long getExpirationInSeconds() {
        return jwtExpirationInSeconds;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            throw new IllegalStateException("JWT secret must be at least 64 bytes for HS512!");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Inside JwtUtil.java
    public String extractPhoneNumber(String token) {
        return extractClaim(token, claims -> claims.get("phone_number", String.class));
    }

}
