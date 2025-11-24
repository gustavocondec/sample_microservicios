package com.universidad.notifications.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universidad.notifications.model.AuthLoginEvent;
import com.universidad.notifications.service.EmailService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuthLoginListener {
  private static final Logger log = LoggerFactory.getLogger(AuthLoginListener.class);

  private final ObjectMapper objectMapper;
  private final EmailService emailService;

  @Value("${app.kafka.topic.auth-login:auth-login}")
  private String topic;

  public AuthLoginListener(ObjectMapper objectMapper, EmailService emailService) {
    this.objectMapper = objectMapper;
    this.emailService = emailService;
  }

  @KafkaListener(topics = "${app.kafka.topic.auth-login:auth-login}", groupId = "${spring.kafka.consumer.group-id:ms-notifications}")
  public void onLogin(ConsumerRecord<String, String> record) {
    try {
      AuthLoginEvent event = objectMapper.readValue(record.value(), AuthLoginEvent.class);
      log.info("Recibido evento de login para {}", event.getEmail());
      emailService.sendLoginEmail(event);
    } catch (Exception ex) {
      log.warn("No se pudo procesar mensaje de login: {}", ex.getMessage());
    }
  }
}
