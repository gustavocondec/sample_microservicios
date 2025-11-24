package com.universidad.facultad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "profesor")
public class Profesor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "profesor_id")
  private Integer id;

  private String nombre;
  private String apellido;
  private String dni;
  private String email;
  private String telefono;
  private String especialidad;

  @Column(name = "titulo_academico")
  private String tituloAcademico;

  @Column(name = "fecha_registro")
  private java.time.OffsetDateTime fechaRegistro;

  private Boolean activo;
}
