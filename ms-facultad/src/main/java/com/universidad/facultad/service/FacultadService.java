package com.universidad.facultad.service;

import com.universidad.facultad.entity.Facultad;
import com.universidad.facultad.repository.FacultadRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FacultadService {
  private final FacultadRepository repository;

  public FacultadService(FacultadRepository repository) {
    this.repository = repository;
  }

  public List<Facultad> findAll() {
    return repository.findAll();
  }

  public Facultad findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facultad no encontrada"));
  }

  public Facultad create(Facultad facultad) {
    facultad.setId(null);
    return repository.save(facultad);
  }

  public Facultad update(Integer id, Facultad facultad) {
    Facultad current = findById(id);
    facultad.setId(current.getId());
    return repository.save(facultad);
  }

  public void delete(Integer id) {
    Facultad current = findById(id);
    repository.delete(current);
  }
}
