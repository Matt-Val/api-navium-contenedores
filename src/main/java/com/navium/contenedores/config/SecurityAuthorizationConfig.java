package com.navium.contenedores.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
public class SecurityAuthorizationConfig {

    @Bean
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> contenedoresAuthorizationCustomizer() {
        return (AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) -> {
            // Swagger UI y OpenAPI - Acceso público para documentación
            auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
            
            // Operaciones de lectura (GET) - ROL_OPERADOR y ROL_CENTRO_MANDO
            auth.requestMatchers(HttpMethod.GET, "/api/contenedores")
                .hasAnyAuthority("ROL_OPERADOR", "ROL_CENTRO_MANDO");

            auth.requestMatchers(HttpMethod.GET, "/api/contenedores/{id}")
                .hasAnyAuthority("ROL_OPERADOR", "ROL_CENTRO_MANDO");
                
            auth.requestMatchers(HttpMethod.GET, "/api/contenedores/patio")
                .hasAnyAuthority("ROL_OPERADOR", "ROL_CENTRO_MANDO");
            
            // Operaciones de escritura crítica (POST, PUT, DELETE) - Solo ROL_CENTRO_MANDO
            auth.requestMatchers(HttpMethod.POST, "/api/contenedores")
                .hasAuthority("ROL_CENTRO_MANDO");
            auth.requestMatchers(HttpMethod.PUT, "/api/contenedores/{id}/estado")
                .hasAuthority("ROL_CENTRO_MANDO");
            auth.requestMatchers(HttpMethod.DELETE, "/api/contenedores/{id}")
                .hasAuthority("ROL_CENTRO_MANDO");
            
            // Cualquier otra petición denegada por defecto
            // auth.anyRequest().denyAll();
        };
    }
}
