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
@Table(name = "facultad")
public class Facultad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "facultad_id")
  private Integer id;

  private String nombre;
  private String descripcion;
  private String ubicacion;
  private String decano;

  @Column(name = "fecha_registro")
  private java.time.OffsetDateTime fechaRegistro;

  private Boolean activo;
}
