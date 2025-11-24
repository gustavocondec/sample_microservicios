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
@Table(name = "seccion")
public class Seccion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "seccion_id")
  private Integer id;

  @Column(name = "curso_id")
  private Integer cursoId;

  @Column(name = "profesor_id")
  private Integer profesorId;

  private String codigo;

  @Column(name = "capacidad_maxima")
  private Integer capacidadMaxima;

  private String aula;
  private String horario;
  private String dias;

  @Column(name = "periodo_academico")
  private String periodoAcademico;

  @Column(name = "fecha_inicio")
  private java.time.LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private java.time.LocalDate fechaFin;

  @Column(name = "fecha_registro")
  private java.time.OffsetDateTime fechaRegistro;

  private Boolean activo;
}
