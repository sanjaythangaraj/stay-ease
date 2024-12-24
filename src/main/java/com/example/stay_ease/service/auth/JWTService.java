package com.example.stay_ease.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.stay_ease.constants.ApplicationConstants.JWT_SECRET_DEFAULT_VALUE;
import static com.example.stay_ease.constants.ApplicationConstants.JWT_SECRET_KEY;

@Service
public class JWTService {

  private Environment environment;

  public JWTService(Environment environment) {
    this.environment = environment;
  }

  private Optional<SecretKey> getSecretKey() {
    if (environment != null) {
      String secret = environment.getProperty(JWT_SECRET_KEY, JWT_SECRET_DEFAULT_VALUE);
      return Optional.of(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)));
    }
    return Optional.empty();
  }

  public String getJWT(Authentication authentication) {
    Optional<SecretKey> optional = getSecretKey();
    return optional.map(secretKey -> Jwts.builder().issuer("stay-ease-api").subject("JWT Token")
        .claim("username", authentication.getName())
        .claim("authorities", authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(",")))
        .issuedAt(new java.util.Date())
        .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
        .signWith(secretKey).compact()).orElse(null);
  }

  public void validateJWT(String jwt) {
    Optional<SecretKey> optional = getSecretKey();
    if (optional.isPresent()) {
      try {
        Claims claims = Jwts.parser().verifyWith(optional.get())
            .build().parseSignedClaims(jwt).getPayload();
        String username = String.valueOf(claims.get("username"));
        String authorities = String.valueOf(claims.get("authorities"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
            AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception exception) {
        throw new BadCredentialsException("Invalid Token received!");
      }
    }
  }
}
