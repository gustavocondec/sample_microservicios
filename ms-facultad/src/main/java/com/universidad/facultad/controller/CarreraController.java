package com.universidad.facultad.controller;

import com.universidad.facultad.entity.Carrera;
import com.universidad.facultad.service.CarreraService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carreras")
public class CarreraController {
  private final CarreraService service;

  public CarreraController(CarreraService service) {
    this.service = service;
  }

  @GetMapping
  public List<Carrera> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Carrera findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Carrera create(@RequestBody Carrera carrera) {
    return service.create(carrera);
  }

  @PutMapping("/{id}")
  public Carrera update(@PathVariable Integer id, @RequestBody Carrera carrera) {
    return service.update(id, carrera);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}
