package com.universidad.facultad.service;

import com.universidad.facultad.entity.Seccion;
import com.universidad.facultad.repository.SeccionRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SeccionService {
  private final SeccionRepository repository;

  public SeccionService(SeccionRepository repository) {
    this.repository = repository;
  }

  public List<Seccion> findAll() {
    return repository.findAll();
  }

  public Seccion findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seccion no encontrada"));
  }

  public Seccion create(Seccion seccion) {
    seccion.setId(null);
    return repository.save(seccion);
  }

  public Seccion update(Integer id, Seccion seccion) {
    Seccion current = findById(id);
    seccion.setId(current.getId());
    return repository.save(seccion);
  }

  public void delete(Integer id) {
    Seccion current = findById(id);
    repository.delete(current);
  }
}
