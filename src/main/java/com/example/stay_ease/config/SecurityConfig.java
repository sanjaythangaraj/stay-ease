package com.example.stay_ease.config;

import com.example.stay_ease.config.filter.JWTTokenGeneratorFilter;
import com.example.stay_ease.config.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

  private final JWTTokenValidatorFilter jwtTokenValidatorFilter;
  private final JWTTokenGeneratorFilter jwtTokenGeneratorFilter;

  public SecurityConfig(JWTTokenValidatorFilter jwtTokenValidatorFilter, JWTTokenGeneratorFilter jwtTokenGeneratorFilter) {
    this.jwtTokenValidatorFilter = jwtTokenValidatorFilter;
    this.jwtTokenGeneratorFilter = jwtTokenGeneratorFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    httpSecurity.addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class);
    httpSecurity.addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class);

    httpSecurity.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
        .requestMatchers(antMatcher(HttpMethod.GET,"/api/hotels/{id}")).hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
        .requestMatchers(HttpMethod.GET,"/api/hotels").hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
        .requestMatchers(HttpMethod.POST,"/api/hotels").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/hotels/{id}")).hasAnyRole("ADMIN")
        .requestMatchers(antMatcher(HttpMethod.PATCH,"/api/hotels/{id}")).hasAnyRole("MANAGER")
        .requestMatchers(HttpMethod.GET, "/api/customers").hasAnyRole("ADMIN")
        .requestMatchers(antMatcher("/api/customers/{id}")).hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
        .requestMatchers(antMatcher("/api/customers/{id}/bookings")).hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
        .requestMatchers(antMatcher("/api/hotels/{id}/book")).hasAnyRole("CUSTOMER", "ADMIN", "MANAGER")
        .requestMatchers(antMatcher("/api/hotels/{id}/bookings")).hasAnyRole("MANAGER", "ADMIN")
        .requestMatchers(antMatcher(HttpMethod.GET,"/api/bookings")).hasAnyRole("ADMIN", "MANAGER")
        .requestMatchers(antMatcher(HttpMethod.GET,"/api/bookings/{id}")).hasAnyRole("ADMIN", "MANAGER")
        .requestMatchers(antMatcher(HttpMethod.DELETE,"/api/bookings/{id}")).hasAnyRole("ADMIN")
        .requestMatchers("/api/login", "/api/register").permitAll());

    return httpSecurity.build();
  }

}
