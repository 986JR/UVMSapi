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
 * JWT Authentication Filter that validates tokens on each request
 * Supports both Android and Web applications

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

            // If valid JWT token exists, extract email
            if (jwt != null && jwtUtil.isTokenValid(jwt)) {
                email = jwtUtil.extractEmail(jwt);
            }

            // If email extracted and no existing authentication
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load vendor details using email
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Validate token against user details
                if (jwtUtil.validateToken(jwt, userDetails)) {

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Set authentication details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Add vendor information to request attributes for easy access
                    addVendorInfoToRequest(request, jwt);
                }
                else {
                    System.out.println("JWT validation failed: ");
                }
            }

        } catch (Exception e) {
            // Log the error but don't block the request
            logger.error("JWT Authentication error: " + e.getMessage());

            // Clear security context on error
            SecurityContextHolder.clearContext();
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Add vendor information from JWT to request attributes
     * Uses database field names for consistency
     */
    private void addVendorInfoToRequest(HttpServletRequest request, String jwt) {
        try {
            // Extract vendor information using database field names
            Integer vendor_id = jwtUtil.extractVendor_id(jwt);
            String first_name = jwtUtil.extractFirst_name(jwt);
            String last_name = jwtUtil.extractLast_name(jwt);
            String phone_number = jwtUtil.extractPhone_number(jwt);
            String company_name = jwtUtil.extractCompany_name(jwt);
            String tin_number = jwtUtil.extractTin_number(jwt);
            String business_address = jwtUtil.extractBusiness_address(jwt);
            String business_type = jwtUtil.extractBusiness_type(jwt);
            Boolean is_active = jwtUtil.extractIs_active(jwt);

            // Set request attributes with database field names
            if (vendor_id != null) request.setAttribute("vendor_id", vendor_id);
            if (first_name != null) request.setAttribute("first_name", first_name);
            if (last_name != null) request.setAttribute("last_name", last_name);
            if (phone_number != null) request.setAttribute("phone_number", phone_number);
            if (company_name != null) request.setAttribute("company_name", company_name);
            if (tin_number != null) request.setAttribute("tin_number", tin_number);
            if (business_address != null) request.setAttribute("business_address", business_address);
            if (business_type != null) request.setAttribute("business_type", business_type);
            if (is_active != null) request.setAttribute("is_active", is_active);

        } catch (Exception e) {
            logger.warn("Could not extract vendor info from JWT: " + e.getMessage());
        }
    }

    /**
     * Skip JWT validation for public endpoints
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // Skip authentication for public endpoints
        return path.startsWith("/api/auth/") ||  // Auth endpoints (login, register)
                path.startsWith("/api/public/") || // Any public endpoints you might have
                path.startsWith("/h2-console/") || // H2 console (for development)
                path.equals("/") ||
                path.equals("/api/auth/register") ||
                // Root path
                path.startsWith("/error");         // Error handling
    }
}