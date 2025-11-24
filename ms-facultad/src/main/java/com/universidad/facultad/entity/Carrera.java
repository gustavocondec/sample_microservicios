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
@Table(name = "carrera")
public class Carrera {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "carrera_id")
  private Integer id;

  @Column(name = "facultad_id")
  private Integer facultadId;

  private String nombre;
  private String descripcion;

  @Column(name = "duracion_semestres")
  private Integer duracionSemestres;

  @Column(name = "titulo_otorgado")
  private String tituloOtorgado;

  @Column(name = "fecha_registro")
  private java.time.OffsetDateTime fechaRegistro;

  private Boolean activo;
}
