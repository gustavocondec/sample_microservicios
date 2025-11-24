package com.universidad.notifications.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthLoginEvent {
  private String email;
  private Integer estudianteId;
  private String loggedAt;
}
