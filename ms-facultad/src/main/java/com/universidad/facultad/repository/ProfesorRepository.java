package com.universidad.facultad.repository;

import com.universidad.facultad.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
}
