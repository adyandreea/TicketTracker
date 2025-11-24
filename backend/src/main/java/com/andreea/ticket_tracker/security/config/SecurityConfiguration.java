package com.andreea.ticket_tracker.security.config;

import com.andreea.ticket_tracker.security.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private static final String AUTH_ALL_ENDPOINTS = "/api/v1/auth/**";
    private static final String BOARDS_ALL_ENDPOINTS = "/api/v1/boards/**";
    private static final String PROJECTS_ALL_ENDPOINTS = "/api/v1/projects/**";
    private static final String TICKETS_ALL_ENDPOINTS = "/api/v1/tickets/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PROJECTS_ALL_ENDPOINTS).hasAnyRole(Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/projects/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, PROJECTS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PROJECTS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, BOARDS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, BOARDS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, BOARDS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, BOARDS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, TICKETS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, TICKETS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, TICKETS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, TICKETS_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, AUTH_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
