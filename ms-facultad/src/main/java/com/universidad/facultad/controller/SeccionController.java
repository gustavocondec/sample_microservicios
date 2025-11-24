package com.universidad.facultad.controller;

import com.universidad.facultad.entity.Seccion;
import com.universidad.facultad.service.SeccionService;
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
@RequestMapping("/secciones")
public class SeccionController {
  private final SeccionService service;

  public SeccionController(SeccionService service) {
    this.service = service;
  }

  @GetMapping
  public List<Seccion> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Seccion findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Seccion create(@RequestBody Seccion seccion) {
    return service.create(seccion);
  }

  @PutMapping("/{id}")
  public Seccion update(@PathVariable Integer id, @RequestBody Seccion seccion) {
    return service.update(id, seccion);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}
