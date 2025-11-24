package com.universidad.matricula.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "matricula")
public class Matricula {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "matricula_id")
  private Integer id;

  @Column(name = "estudiante_id")
  private Integer estudianteId;

  @Column(name = "seccion_id")
  private Integer seccionId;

  @Column(name = "fecha_matricula")
  private LocalDate fechaMatricula;

  private String estado;
  private BigDecimal costo;

  @Column(name = "metodo_pago")
  private String metodoPago;

  @Column(name = "fecha_registro")
  private OffsetDateTime fechaRegistro;
}
