package com.codelevels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "examenes_docente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeacherExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String titulo;

    @Column(nullable = false, length = 40)
    private String dificultad;

    @Column(nullable = false, length = 70)
    private String tema;

    @Column(nullable = false, length = 900)
    private String descripcion;

    @Column(nullable = false)
    private Integer tiempoMinutos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String preguntasJson;

    @Column(length = 500)
    @Builder.Default
    private String archivoNombre = "";

    @Column(length = 700)
    @Builder.Default
    private String archivoUrl = "";

    @Column(length = 40)
    @Builder.Default
    private String tipoArchivo = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private AppUser docente;

    @Column(nullable = false)
    @Builder.Default
    private boolean publicado = true;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();
}
