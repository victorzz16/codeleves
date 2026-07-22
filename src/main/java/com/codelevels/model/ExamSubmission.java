package com.codelevels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregas_examen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExamSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examen_id", nullable = false)
    private TeacherExam examen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private AppUser alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private AppUser docente;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String respuestaTexto;

    @Column(length = 500)
    @Builder.Default
    private String archivoNombre = "";

    @Column(length = 700)
    @Builder.Default
    private String archivoUrl = "";

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column
    private Integer calificacion;

    @Column(length = 1000)
    @Builder.Default
    private String retroalimentacion = "";

    @Column(nullable = false)
    @Builder.Default
    private boolean notificadoAlumno = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime enviadoEn = LocalDateTime.now();

    private LocalDateTime calificadoEn;
}
