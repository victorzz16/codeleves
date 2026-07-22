package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.AssignedTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedTaskRepository extends JpaRepository<AssignedTask, Long> {
    List<AssignedTask> findByAlumnoOrAlumnoIsNullOrderByCreadoEnDesc(AppUser alumno);
    List<AssignedTask> findByDocenteOrderByCreadoEnDesc(AppUser docente);
}
