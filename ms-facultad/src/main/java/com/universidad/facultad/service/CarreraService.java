package com.universidad.facultad.service;

import com.universidad.facultad.entity.Carrera;
import com.universidad.facultad.repository.CarreraRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarreraService {
  private final CarreraRepository repository;

  public CarreraService(CarreraRepository repository) {
    this.repository = repository;
  }

  public List<Carrera> findAll() {
    return repository.findAll();
  }

  public Carrera findById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrera no encontrada"));
  }

  public Carrera create(Carrera carrera) {
    carrera.setId(null);
    return repository.save(carrera);
  }

  public Carrera update(Integer id, Carrera carrera) {
    Carrera current = findById(id);
    carrera.setId(current.getId());
    return repository.save(carrera);
  }

  public void delete(Integer id) {
    Carrera current = findById(id);
    repository.delete(current);
  }
}
