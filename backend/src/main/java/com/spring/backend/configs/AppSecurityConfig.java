package com.spring.backend.configs;

import com.spring.backend.exception.CustomAccessDeniedHandler;
import com.spring.backend.exception.CustomAuthenticationEntryPoint;
import com.spring.backend.filter.JwtAuthenticationFilter;
import com.spring.backend.modules.auth.JwtService;
import com.spring.backend.modules.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppSecurityConfig {

  CustomAccessDeniedHandler customAccessDeniedHandler;
  CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  JwtService jwtService;

  static String[] SWAGGER_WHITELIST = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
  static String[] PUBLIC_ENDPOINTS = {"/", "/avatars/**"};
  static String[] AUTH_ENDPOINTS = {"/api/v1/auth/login", "/api/v1/auth/refresh-token"};

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);

    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(HttpMethod.GET, SWAGGER_WHITELIST)
                .permitAll()
                .requestMatchers(HttpMethod.POST, AUTH_ENDPOINTS)
                .permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

    http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.exceptionHandling(
        ex ->
            ex.authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  JwtAuthenticationFilter jwtAuthenticationFilter(UserService userService) {
    return new JwtAuthenticationFilter(jwtService, userService);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsFilter corsFilter() {
    CorsConfiguration cors = new CorsConfiguration();
    cors.addAllowedOrigin("*");
    cors.addAllowedMethod("*");
    cors.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cors);

    return new CorsFilter(source);
  }
}
