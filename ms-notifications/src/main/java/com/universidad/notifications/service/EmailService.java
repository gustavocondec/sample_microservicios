package com.universidad.notifications.service;

import com.universidad.notifications.model.AuthLoginEvent;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
  private static final Logger log = LoggerFactory.getLogger(EmailService.class);
  private static final String RESEND_URL = "https://api.resend.com/emails";

  private final RestTemplate restTemplate;

  @Value("${resend.api-key:}")
  private String apiKey;

  @Value("${resend.from:no-reply@universidad.local}")
  private String from;

  public EmailService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void sendLoginEmail(AuthLoginEvent event) {
    if (apiKey == null || apiKey.isBlank()) {
      log.warn("RESEND_API_KEY no configurado, no se envía correo para {}", event.getEmail());
      return;
    }

    Map<String, Object> body = Map.of(
        "from", from,
        "to", List.of(event.getEmail()),
        "subject", "Inicio de sesión en tu cuenta",
        "html", buildHtml(event)
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(RESEND_URL, new HttpEntity<>(body, headers), String.class);
      log.info("Correo enviado a {} status {}", event.getEmail(), response.getStatusCode());
    } catch (Exception ex) {
      log.warn("Error enviando correo a {}: {}", event.getEmail(), ex.getMessage());
    }
  }

  private String buildHtml(AuthLoginEvent event) {
    String loggedAt = event.getLoggedAt() != null ? event.getLoggedAt() : "";
    return "<p>Hola,</p>"
        + "<p>Se detectó un inicio de sesión en tu cuenta.</p>"
        + "<ul>"
        + "<li>Email: " + event.getEmail() + "</li>"
        + "<li>Estudiante ID: " + event.getEstudianteId() + "</li>"
        + "<li>Fecha/Hora: " + loggedAt + "</li>"
        + "</ul>"
        + "<p>Si no fuiste tú, por favor contacta al soporte.</p>";
  }
}
