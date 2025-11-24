package com.universidad.matricula.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Valida el token emitido por ms-auth (Base64 email:estudianteId) solo para POST /matriculas.
 */
@Component
@Order(1)
public class TokenAuthFilter extends OncePerRequestFilter {
  private static final String TARGET_PATH_SUFFIX = "/matriculas";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!isProtectedEndpoint(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      unauthorized(response, "Falta header Authorization Bearer");
      return;
    }

    String token = authHeader.substring(7);
    String decoded;
    try {
      decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    } catch (IllegalArgumentException ex) {
      unauthorized(response, "Token inválido (no es Base64)");
      return;
    }

    int separatorIdx = decoded.indexOf(':');
    if (separatorIdx < 1 || separatorIdx == decoded.length() - 1) {
      unauthorized(response, "Formato de token inválido");
      return;
    }

    String email = decoded.substring(0, separatorIdx);
    String idPart = decoded.substring(separatorIdx + 1);
    if (!email.contains("@") || !idPart.matches("\\d+")) {
      unauthorized(response, "Token inválido");
      return;
    }

    // Token es válido, continuar.
    filterChain.doFilter(request, response);
  }

  private boolean isProtectedEndpoint(HttpServletRequest request) {
    return "POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().endsWith(TARGET_PATH_SUFFIX);
  }

  private void unauthorized(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"message\":\"" + message + "\"}");
  }
}
