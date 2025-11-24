package com.universidad.matricula.service;

import com.universidad.matricula.entity.Matricula;
import com.universidad.matricula.repository.MatriculaRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatriculaService {
  private static final Set<String> ESTADOS_VALIDOS =
      new HashSet<>(Arrays.asList("PENDIENTE", "PAGADO", "ANULADO", "COMPLETADO"));

  private final MatriculaRepository repository;

  public MatriculaService(MatriculaRepository repository) {
    this.repository = repository;
  }

  public List<Matricula> findAll() {
    return repository.findAll();
  }

  public Matricula findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula no encontrada"));
  }

  public Matricula create(Matricula matricula) {
    if (repository.existsByEstudianteIdAndSeccionId(matricula.getEstudianteId(), matricula.getSeccionId())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Matrícula ya existe para el estudiante y sección");
    }
    if (matricula.getFechaMatricula() == null) {
      matricula.setFechaMatricula(LocalDate.now());
    }
    matricula.setEstado(normalizaEstado(matricula.getEstado()));
    validaEstado(matricula.getEstado());
    validaReferencias(matricula);
    matricula.setId(null);
    return repository.save(matricula);
  }

  public Matricula update(Integer id, Matricula matricula) {
    Matricula current = findById(id);
    matricula.setId(current.getId());
    matricula.setEstado(normalizaEstado(matricula.getEstado()));
    validaEstado(matricula.getEstado());
    validaReferencias(matricula);
    return repository.save(matricula);
  }

  public void delete(Integer id) {
    Matricula current = findById(id);
    repository.delete(current);
  }

  private String normalizaEstado(String estado) {
    if (estado == null || estado.isBlank()) {
      return "PENDIENTE";
    }
    return estado.trim().toUpperCase();
  }

  private void validaEstado(String estado) {
    if (!ESTADOS_VALIDOS.contains(estado)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Estado inválido. Valores permitidos: " + ESTADOS_VALIDOS);
    }
  }

  private void validaReferencias(Matricula matricula) {
    if (matricula.getEstudianteId() == null || matricula.getEstudianteId() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "estudianteId debe ser mayor a 0");
    }
    if (matricula.getSeccionId() == null || matricula.getSeccionId() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "seccionId debe ser mayor a 0");
    }
    if (matricula.getCosto() != null && matricula.getCosto().signum() < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "costo no puede ser negativo");
    }
  }
}
