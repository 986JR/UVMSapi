package com.uvms.apiuvms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for validating tokens on each request
 * Supports both Vendor and Admin JWTs
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Extract JWT token from Authorization header
            String authHeader = request.getHeader("Authorization");
            String jwt = jwtUtil.extractTokenFromHeader(authHeader);
            String email = null;

            if (jwt != null && jwtUtil.isTokenValid(jwt)) {
                email = jwtUtil.extractEmail(jwt);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Add vendor/admin info to request attributes
                    addJwtInfoToRequest(request, jwt);
                }
            }

        } catch (Exception e) {
            logger.error("JWT Authentication error: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private void addJwtInfoToRequest(HttpServletRequest request, String jwt) {
        try {
            // Vendor info
            Integer vendorId = jwtUtil.extractVendorId(jwt);
            String firstName = jwtUtil.extractFirstName(jwt);
            String lastName = jwtUtil.extractLastName(jwt);
            String phoneNumber = jwtUtil.extractPhoneNumber(jwt);
            String companyName = jwtUtil.extractCompanyName(jwt);
            String tinNumber = jwtUtil.extractClaim(jwt, claims -> claims.get("tin_number", String.class));
            String businessAddress = jwtUtil.extractClaim(jwt, claims -> claims.get("business_address", String.class));
            String businessType = jwtUtil.extractClaim(jwt, claims -> claims.get("business_type", String.class));
            Boolean vendorActive = jwtUtil.extractVendorIsActive(jwt);

            if (vendorId != null) request.setAttribute("vendor_id", vendorId);
            if (firstName != null) request.setAttribute("first_name", firstName);
            if (lastName != null) request.setAttribute("last_name", lastName);
            if (phoneNumber != null) request.setAttribute("phone_number", phoneNumber);
            if (companyName != null) request.setAttribute("company_name", companyName);
            if (tinNumber != null) request.setAttribute("tin_number", tinNumber);
            if (businessAddress != null) request.setAttribute("business_address", businessAddress);
            if (businessType != null) request.setAttribute("business_type", businessType);
            if (vendorActive != null) request.setAttribute("is_active", vendorActive);

            // Admin info
            Integer adminId = jwtUtil.extractAdminId(jwt);
            String adminName = jwtUtil.extractAdminName(jwt);
            String adminRole = jwtUtil.extractAdminRole(jwt);
            Boolean adminActive = jwtUtil.extractAdminIsActive(jwt);

            if (adminId != null) request.setAttribute("admin_id", adminId);
            if (adminName != null) request.setAttribute("admin_name", adminName);
            if (adminRole != null) request.setAttribute("admin_role", adminRole);
            if (adminActive != null) request.setAttribute("admin_is_active", adminActive);

        } catch (Exception e) {
            logger.warn("Could not extract JWT info: " + e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.startsWith("/api/public/")
                || path.startsWith("/h2-console/") || path.equals("/") || path.startsWith("/error");
    }
}
