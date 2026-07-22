package com.codelevels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_retos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChallengeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String titulo;

    @Column(nullable = false, length = 40)
    private String dificultad;

    @Column(nullable = false, length = 70)
    private String categoria;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(length = 1000)
    private String justificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private AppUser docente;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column(length = 700)
    @Builder.Default
    private String respuestaAdmin = "";

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creadoEn = LocalDateTime.now();

    private LocalDateTime revisadoEn;
}
