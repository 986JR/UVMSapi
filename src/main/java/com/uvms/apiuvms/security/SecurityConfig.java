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
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        // Public pages
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html","/dashboard2.html","/forgotPassword.html").permitAll()
                        // Static assets
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Public auth endpoints
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/colleges/**","/api/tenders/**","/api/colleges","/api/tenders").permitAll()
                        .requestMatchers("/api/policies/**","/api/policies","/api/policies/active","/api/policies/scope/**","/api/policies").permitAll()
                        // Protected endpoints
                        .requestMatchers("/api/protected/**").authenticated()
                        .requestMatchers("/api/dashboard").authenticated()
                        // Anything else
                        .anyRequest().authenticated()
                )
                // JWT filter before username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
