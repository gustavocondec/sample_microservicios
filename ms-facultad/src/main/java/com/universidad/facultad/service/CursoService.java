package com.universidad.facultad.service;

import com.universidad.facultad.entity.Curso;
import com.universidad.facultad.repository.CursoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CursoService {
  private final CursoRepository repository;

  public CursoService(CursoRepository repository) {
    this.repository = repository;
  }

  public List<Curso> findAll() {
    return repository.findAll();
  }

  public Curso findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
  }

  public Curso create(Curso curso) {
    curso.setId(null);
    return repository.save(curso);
  }

  public Curso update(Integer id, Curso curso) {
    Curso current = findById(id);
    curso.setId(current.getId());
    return repository.save(curso);
  }

  public void delete(Integer id) {
    Curso current = findById(id);
    repository.delete(current);
  }
}
