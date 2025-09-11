package dev.louisa.jam.hub.infrastructure.security;

import dev.louisa.jam.hub.infrastructure.logging.MDCLoggingFilter;
import dev.louisa.jam.hub.infrastructure.security.exception.ForbiddenExceptionHandler;
import dev.louisa.jam.hub.infrastructure.security.exception.UnauthorizedExceptionHandler;
import dev.louisa.jam.hub.infrastructure.security.jwt.JHubJwtValidator;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtAuthenticationFilter;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtValidator;
import dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    public static final List<Map.Entry<String, String>> UNSECURED_URI_LIST =  List.of(
            Map.entry("/api/v1/registrations", "POST")
    );

    private final SecurityLevelResolver securityLevelResolver;



    //TODO: Make a Bean/Config for the custom filters.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           MDCLoggingFilter mdcLoggingFilter,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           UnauthorizedExceptionHandler unauthorizedExceptionHandler,
                                           ForbiddenExceptionHandler forbiddenExceptionHandler) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(STATELESS))
                .addFilterBefore(mdcLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(unauthorizedExceptionHandler)
                        .accessDeniedHandler(forbiddenExceptionHandler))
                .securityMatcher("/api/**")
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers((request) -> securityLevelResolver.isUnsecured(request.getRequestURI(), request.getMethod())).permitAll()
                        .anyRequest().authenticated()
                );
        
        return http.build();
    }

    @Bean
    public JwtValidator jwtValidator() {
        final RSAPublicKey key = RSAKeyReader.readPublicKeyFromFile("jwk-set/19b14038-11df-43c5-a03c-db39a55b4e5b.key.pub");
        return new JHubJwtValidator(key);
    }
}
