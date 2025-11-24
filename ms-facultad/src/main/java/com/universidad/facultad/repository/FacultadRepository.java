package com.universidad.facultad.repository;

import com.universidad.facultad.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultadRepository extends JpaRepository<Facultad, Integer> {
}
