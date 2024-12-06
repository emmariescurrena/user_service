package com.emmariescurrena.bookesy.user_service.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {

        http
            .csrf((csrf) -> csrf.disable())
            .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
            .authorizeExchange(authz -> authz
                .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyExchange().authenticated()
            ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();

    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
