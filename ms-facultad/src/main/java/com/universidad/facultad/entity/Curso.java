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
@Table(name = "curso")
public class Curso {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "curso_id")
  private Integer id;

  @Column(name = "carrera_id")
  private Integer carreraId;

  private String codigo;
  private String nombre;
  private String descripcion;
  private Integer creditos;

  @Column(name = "nivel_semestre")
  private Integer nivelSemestre;

  @Column(name = "fecha_registro")
  private java.time.OffsetDateTime fechaRegistro;

  private Boolean activo;
}
