package com.uvms.apiuvms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        // Public pages (served by Spring Boot)
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html",
                                "/dashboard2.html", "/forgotPassword.html").permitAll()
                        // Static assets
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        // Actuator health check (for Render)
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // Public auth endpoints
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // Public API endpoints
                        .requestMatchers("/api/colleges/**", "/api/tenders/**","/api/colleges").permitAll()
                        .requestMatchers("/api/policies/**").permitAll()
                        // Protected endpoints
                        .requestMatchers("/api/protected/**").authenticated()
                        .requestMatchers("/api/dashboard").authenticated()
                        .requestMatchers("/api/auth/profile", "/api/auth/refresh", "/api/auth/logout").authenticated()
                        // Anything else requires authentication
                        .anyRequest().authenticated()
                )
                // JWT filter before username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins (add your Render domain here)
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",           // Local development
                "https://*.onrender.com",       // Any Render subdomain
                "https://986jr.github.io"
        ));

        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Expose headers (so frontend can read them)
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        // Allow credentials (needed for cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}