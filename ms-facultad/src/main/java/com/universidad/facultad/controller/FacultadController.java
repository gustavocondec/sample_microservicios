package com.universidad.facultad.controller;

import com.universidad.facultad.entity.Facultad;
import com.universidad.facultad.service.FacultadService;
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
@RequestMapping("/facultades")
public class FacultadController {
  private final FacultadService service;

  public FacultadController(FacultadService service) {
    this.service = service;
  }

  @GetMapping
  public List<Facultad> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Facultad findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Facultad create(@RequestBody Facultad facultad) {
    return service.create(facultad);
  }

  @PutMapping("/{id}")
  public Facultad update(@PathVariable Integer id, @RequestBody Facultad facultad) {
    return service.update(id, facultad);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}
