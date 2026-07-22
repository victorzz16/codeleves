package com.codelevels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "progreso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private AppUser usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reto_id")
    private Challenge reto;

    @Column(nullable = false)
    private boolean completado;

    @Column(nullable = false)
    private int puntos;

    @Column(nullable = false)
    private int intentos;

    @Column(nullable = false)
    private int precision;

    @Column(nullable = false)
    private LocalDateTime actualizadoEn;
}
