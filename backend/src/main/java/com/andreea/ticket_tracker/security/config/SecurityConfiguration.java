package com.andreea.ticket_tracker.security.config;

import com.andreea.ticket_tracker.security.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtProvider tokenGenerator;
    private final CustomUserDetailsService customUserDetailsService;

    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String AUTH_ALL_ENDPOINTS = "/api/v1/auth/**";
    private static final String BOARDS_ALL_ENDPOINTS = "/api/v1/boards/**";
    private static final String PROJECTS_ALL_ENDPOINTS = "/api/v1/projects/**";
    private static final String TICKETS_ALL_ENDPOINTS = "/api/v1/tickets/**";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PROJECTS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, PROJECTS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, PROJECTS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, BOARDS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.GET, BOARDS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, BOARDS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, BOARDS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, TICKETS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.GET, TICKETS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, TICKETS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, TICKETS_ALL_ENDPOINTS).hasAnyAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, AUTH_ALL_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter(tokenGenerator, customUserDetailsService);
    }
}
