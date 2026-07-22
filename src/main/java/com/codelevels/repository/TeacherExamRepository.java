package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.TeacherExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherExamRepository extends JpaRepository<TeacherExam, Long> {
    List<TeacherExam> findByPublicadoTrueOrderByCreadoEnDesc();
    List<TeacherExam> findByDocenteOrderByCreadoEnDesc(AppUser docente);
}
