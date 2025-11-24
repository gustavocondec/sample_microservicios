package com.universidad.matricula.controller;

import com.universidad.matricula.entity.Matricula;
import com.universidad.matricula.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/matriculas")
@SecurityRequirement(name = "bearerAuth")
public class MatriculaController {
  private final MatriculaService service;

  public MatriculaController(MatriculaService service) {
    this.service = service;
  }

  @GetMapping
  public List<Matricula> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Matricula findById(@PathVariable Integer id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Crear matrícula",
      description = "Crea una matrícula nueva. Protegido: enviar Authorization: Bearer <token emitido por ms-auth>. "
          + "Estado válido: PENDIENTE, PAGADO, ANULADO o COMPLETADO.",
      security = { @SecurityRequirement(name = "bearerAuth") }
  )
  public Matricula create(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Payload de matrícula (sin enviar id, se genera automáticamente)",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = Matricula.class),
              examples = {
                  @ExampleObject(
                      name = "Nuevo registro (IDs seed válidos sin conflicto)",
                      value = """
                          {
                            "estudianteId": 1,
                            "seccionId": 2,
                            "estado": "PAGADO",
                            "costo": 1200.00,
                            "metodoPago": "TARJETA"
                          }
                          """
                  )
              }
          )
      )
      @RequestBody Matricula matricula) {
    return service.create(matricula);
  }

  @PutMapping("/{id}")
  public Matricula update(@PathVariable Integer id, @RequestBody Matricula matricula) {
    return service.update(id, matricula);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    service.delete(id);
  }
}
