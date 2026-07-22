package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.Challenge;
import com.codelevels.model.Progress;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.ChallengeRepository;
import com.codelevels.repository.ProgressRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alumno/progreso")
@RequiredArgsConstructor
public class ProgressRestController {
    private final AppUserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ProgressRepository progressRepository;

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody ProgressRequest request, Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        Challenge reto = challengeRepository.findById(request.getRetoId()).orElseThrow();

        Progress progress = progressRepository.findByUsuarioAndReto(user, reto)
                .orElse(Progress.builder()
                        .usuario(user)
                        .reto(reto)
                        .intentos(0)
                        .puntos(0)
                        .precision(0)
                        .completado(false)
                        .actualizadoEn(LocalDateTime.now())
                        .build());

        progress.setIntentos(progress.getIntentos() + 1);
        progress.setCompletado(request.isCompletado());
        progress.setPrecision(request.isCompletado() ? 100 : 0);
        if (request.isCompletado() && progress.getPuntos() == 0) {
            int pts = switch (reto.getDificultad()) {
                case "Básico" -> 100;
                case "Medio" -> 150;
                default -> 200;
            };
            progress.setPuntos(pts);
        }
        progress.setActualizadoEn(LocalDateTime.now());
        progressRepository.save(progress);
        return ResponseEntity.ok(progress);
    }

    @Data
    public static class ProgressRequest {
        private Long retoId;
        private boolean completado;
    }
}
