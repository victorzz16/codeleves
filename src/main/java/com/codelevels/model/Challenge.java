package com.codelevels.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 40)
    private String dificultad;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bloquesJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String solucionJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pistasJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String codigoJava;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean premium = false;
}
