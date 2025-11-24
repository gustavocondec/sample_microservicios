package com.universidad.facultad.controller;

import com.universidad.facultad.entity.Curso;
import com.universidad.facultad.service.CursoService;
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
@RequestMapping("/cursos")
public class CursoController {
  private final CursoService service;

  public CursoController(CursoService service) {
    this.service = service;
  }

  @GetMapping
  public List<Curso> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Curso findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Curso create(@RequestBody Curso curso) {
    return service.create(curso);
  }

  @PutMapping("/{id}")
  public Curso update(@PathVariable Integer id, @RequestBody Curso curso) {
    return service.update(id, curso);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}
