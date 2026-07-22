package com.codelevels.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role rol;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean premium = false;

    private java.time.LocalDateTime premiumDesde;

    private java.time.LocalDateTime premiumHasta;

    @Column(length = 500)
    @Builder.Default
    private String fotoUrl = "";

    @Column(length = 500)
    @Builder.Default
    private String portadaUrl = "";

    @Column(length = 700)
    @Builder.Default
    private String descripcion = "";

    @Column(length = 80)
    @Builder.Default
    private String carrera = "Ingeniería de Sistemas";

    @Column(length = 80)
    @Builder.Default
    private String sede = "UTP Ate";
}
