package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.ExamSubmission;
import com.codelevels.model.TeacherExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamSubmissionRepository extends JpaRepository<ExamSubmission, Long> {
    List<ExamSubmission> findByDocenteOrderByEnviadoEnDesc(AppUser docente);
    List<ExamSubmission> findByAlumnoOrderByEnviadoEnDesc(AppUser alumno);
    Optional<ExamSubmission> findByAlumnoAndExamen(AppUser alumno, TeacherExam examen);
    long countByAlumnoAndEstado(AppUser alumno, String estado);
    long countByDocenteAndEstado(AppUser docente, String estado);
}
