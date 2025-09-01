package com.uvms.apiuvms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Utility class for token generation, validation and information extraction
 * Supports both Android and Web applications
 */
@Component
public class JwtUtil {

    // JWT Configuration - these will be loaded from application.properties
    @Value("${jwt.secret:uvmsSecretKeyForJWTTokenGenerationThatShouldBeLongEnoughAndSecure2024}")
    private String jwtSecret;

    @Value("${jwt.expiration:60*60}") // 24 hours in seconds
    private Long jwtExpirationInSeconds;

    /**
     * Generate JWT token for authenticated vendor
     * @param userDetails Spring Security UserDetails (email-based)
     * @return JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername()); // username = email
    }

    /**
     * Generate JWT token with vendor information
     * @param userDetails Spring Security UserDetails
     * @param vendor Vendor entity with all information
     * @return JWT token string
     */
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

    /**
     * Create JWT token with claims and subject
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // This will be the email
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract email (username) from JWT token
     * @param token JWT token
     * @return email address
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract vendor_id from JWT token
     * @param token JWT token
     * @return vendor_id
     */
    public Integer extractVendor_id(String token) {
        return extractClaim(token, claims -> claims.get("vendor_id", Integer.class));
    }

    /**
     * Extract first_name from JWT token
     * @param token JWT token
     * @return first_name
     */
    public String extractFirst_name(String token) {
        return extractClaim(token, claims -> claims.get("first_name", String.class));
    }

    /**
     * Extract last_name from JWT token
     * @param token JWT token
     * @return last_name
     */
    public String extractLast_name(String token) {
        return extractClaim(token, claims -> claims.get("last_name", String.class));
    }

    /**
     * Extract phone_number from JWT token
     * @param token JWT token
     * @return phone_number
     */
    public String extractPhone_number(String token) {
        return extractClaim(token, claims -> claims.get("phone_number", String.class));
    }

    /**
     * Extract company_name from JWT token
     * @param token JWT token
     * @return company_name
     */
    public String extractCompany_name(String token) {
        return extractClaim(token, claims -> claims.get("company_name", String.class));
    }

    /**
     * Extract tin_number from JWT token
     * @param token JWT token
     * @return tin_number
     */
    public String extractTin_number(String token) {
        return extractClaim(token, claims -> claims.get("tin_number", String.class));
    }

    /**
     * Extract business_address from JWT token
     * @param token JWT token
     * @return business_address
     */
    public String extractBusiness_address(String token) {
        return extractClaim(token, claims -> claims.get("business_address", String.class));
    }

    /**
     * Extract business_type from JWT token
     * @param token JWT token
     * @return business_type
     */
    public String extractBusiness_type(String token) {
        return extractClaim(token, claims -> claims.get("business_type", String.class));
    }

    /**
     * Extract is_active status from JWT token
     * @param token JWT token
     * @return is_active status
     */
    public Boolean extractIs_active(String token) {
        return extractClaim(token, claims -> claims.get("is_active", Boolean.class));
    }

    /**
     * Extract expiration date from JWT token
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract any claim from JWT token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from JWT token
     */
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

    /**
     * Check if JWT token is expired
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true; // Consider invalid tokens as expired
        }
    }

    /**
     * Validate JWT token against UserDetails
     * @param token JWT token
     * @param userDetails Spring Security UserDetails
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if JWT token is valid (not expired and properly formatted)
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public Boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get JWT expiration time in seconds
     * @return expiration time in seconds
     */
    public Long getExpirationInSeconds() {
        return jwtExpirationInSeconds;
    }

    /**
     * Get signing key for JWT
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract token from Authorization header
     * @param authHeader Authorization header value
     * @return JWT token without "Bearer " prefix, or null if invalid
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}