package com.universidad.matricula.repository;

import com.universidad.matricula.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
  boolean existsByEstudianteIdAndSeccionId(Integer estudianteId, Integer seccionId);
}
