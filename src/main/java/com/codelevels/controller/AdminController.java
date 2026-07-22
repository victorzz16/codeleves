package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.Challenge;
import com.codelevels.model.Role;
import com.codelevels.model.ChallengeRequest;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.ChallengeRepository;
import com.codelevels.repository.ChallengeRequestRepository;
import com.codelevels.repository.ProgressRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AppUserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ProgressRepository progressRepository;
    private final ChallengeRequestRepository challengeRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long alumnos = userRepository.findAll().stream().filter(u -> u.getRol() == Role.ALUMNO).count();
        long docentes = userRepository.findAll().stream().filter(u -> u.getRol() == Role.DOCENTE).count();
        long admins = userRepository.findAll().stream().filter(u -> u.getRol() == Role.ADMIN).count();
        long activos = userRepository.findAll().stream().filter(u -> u.isActivo()).count();

        model.addAttribute("alumnos", alumnos);
        model.addAttribute("docentes", docentes);
        model.addAttribute("admins", admins);
        model.addAttribute("activos", activos);
        model.addAttribute("usuarios", userRepository.findAll());
        model.addAttribute("retos", challengeRepository.findAll());
        model.addAttribute("totalRetos", challengeRepository.count());
        model.addAttribute("totalProgresos", progressRepository.count());
        model.addAttribute("retosCompletados", progressRepository.countCompleted());
        model.addAttribute("progresos", progressRepository.findAll());
        model.addAttribute("solicitudes", challengeRequestRepository.findAllByOrderByCreadoEnDesc());
        model.addAttribute("solicitudesPendientes", challengeRequestRepository.countByEstado("PENDIENTE"));
        return "admin/dashboard";
    }



    @PostMapping("/solicitudes-retos/revisar")
    public String revisarSolicitud(@RequestParam Long solicitudId,
                                   @RequestParam String estado,
                                   @RequestParam(required = false, defaultValue = "") String respuestaAdmin,
                                   RedirectAttributes redirectAttributes) {
        try {
            ChallengeRequest solicitud = challengeRequestRepository.findById(solicitudId).orElseThrow();
            String estadoNormalizado = estado == null ? "PENDIENTE" : estado.trim().toUpperCase();
            if (!List.of("PENDIENTE", "APROBADA", "RECHAZADA").contains(estadoNormalizado)) {
                estadoNormalizado = "PENDIENTE";
            }
            solicitud.setEstado(estadoNormalizado);
            solicitud.setRespuestaAdmin(respuestaAdmin == null ? "" : respuestaAdmin.trim());
            solicitud.setRevisadoEn(LocalDateTime.now());
            challengeRequestRepository.save(solicitud);
            redirectAttributes.addFlashAttribute("okSolicitudAdmin", "Solicitud actualizada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorSolicitudAdmin", "No se pudo actualizar la solicitud.");
        }
        return "redirect:/admin/dashboard#solicitudes-retos";
    }

    @PostMapping("/usuarios")
    public String crearUsuario(@RequestParam String nombre,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam Role rol,
                               @RequestParam(required = false, defaultValue = "UTP Ate") String sede,
                               @RequestParam(required = false, defaultValue = "Ingeniería de Sistemas") String carrera,
                               RedirectAttributes redirectAttributes) {
        try {
            String nombreLimpio = nombre == null ? "" : nombre.trim();
            String emailLimpio = email == null ? "" : email.trim().toLowerCase();
            String clave = password == null ? "" : password.trim();

            if (nombreLimpio.isBlank() || emailLimpio.isBlank() || clave.isBlank()) {
                redirectAttributes.addFlashAttribute("errorUsuario", "Completa nombre, correo y contraseña.");
                return "redirect:/admin/dashboard#nuevo-usuario";
            }

            if (clave.length() < 6) {
                redirectAttributes.addFlashAttribute("errorUsuario", "La contraseña debe tener como mínimo 6 caracteres.");
                return "redirect:/admin/dashboard#nuevo-usuario";
            }

            if (rol == Role.ALUMNO) {
                redirectAttributes.addFlashAttribute("errorUsuario", "Desde este módulo solo se crean cuentas DOCENTE o ADMIN.");
                return "redirect:/admin/dashboard#nuevo-usuario";
            }

            if (userRepository.existsByEmail(emailLimpio)) {
                redirectAttributes.addFlashAttribute("errorUsuario", "Ya existe una cuenta registrada con ese correo.");
                return "redirect:/admin/dashboard#nuevo-usuario";
            }

            AppUser usuario = AppUser.builder()
                    .nombre(nombreLimpio)
                    .email(emailLimpio)
                    .password(passwordEncoder.encode(clave))
                    .rol(rol)
                    .activo(true)
                    .sede(sede == null || sede.trim().isBlank() ? "UTP Ate" : sede.trim())
                    .carrera(carrera == null || carrera.trim().isBlank() ? "Ingeniería de Sistemas" : carrera.trim())
                    .descripcion(rol == Role.ADMIN
                            ? "Administrador de la plataforma CodeLevels."
                            : "Docente encargado del seguimiento académico en CodeLevels.")
                    .build();

            userRepository.save(usuario);
            redirectAttributes.addFlashAttribute("okUsuario", "Cuenta " + rol + " creada correctamente. Ya puede iniciar sesión.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorUsuario", "No se pudo crear la cuenta. Revisa los datos ingresados.");
        }
        return "redirect:/admin/dashboard#usuarios";
    }

    @PostMapping("/retos")
    public String crearReto(@RequestParam String titulo,
                            @RequestParam String dificultad,
                            @RequestParam String categoria,
                            @RequestParam String descripcion,
                            @RequestParam String bloques,
                            @RequestParam String solucion,
                            @RequestParam String pistas,
                            @RequestParam String codigoJava,
                            RedirectAttributes redirectAttributes) {
        try {
            if (challengeRepository.existsByTitulo(titulo.trim())) {
                redirectAttributes.addFlashAttribute("errorReto", "Ya existe un reto con ese título.");
                return "redirect:/admin/dashboard#nuevo-reto";
            }

            List<String> bloquesList = limpiarLineas(bloques);
            List<String> pistasList = limpiarLineas(pistas);
            List<Integer> solucionList = parsearSolucion(solucion, bloquesList.size());

            if (titulo.trim().isBlank() || dificultad.trim().isBlank() || categoria.trim().isBlank()
                    || descripcion.trim().isBlank() || codigoJava.trim().isBlank()) {
                redirectAttributes.addFlashAttribute("errorReto", "Completa todos los campos obligatorios.");
                return "redirect:/admin/dashboard#nuevo-reto";
            }

            if (bloquesList.size() < 3) {
                redirectAttributes.addFlashAttribute("errorReto", "Agrega al menos 3 bloques para el reto.");
                return "redirect:/admin/dashboard#nuevo-reto";
            }

            if (pistasList.isEmpty()) {
                pistasList = List.of("Identifica primero la entrada, luego el proceso y al final la salida.");
            }

            Challenge reto = Challenge.builder()
                    .titulo(titulo.trim())
                    .dificultad(dificultad.trim())
                    .categoria(categoria.trim())
                    .descripcion(descripcion.trim())
                    .bloquesJson(toJson(bloquesList))
                    .solucionJson(toJson(solucionList))
                    .pistasJson(toJson(pistasList))
                    .codigoJava(codigoJava.trim())
                    .build();

            challengeRepository.save(reto);
            redirectAttributes.addFlashAttribute("okReto", "Reto creado correctamente. Ya está disponible para los alumnos.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorReto", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorReto", "No se pudo crear el reto. Revisa los datos ingresados.");
        }
        return "redirect:/admin/dashboard#retos";
    }

    private List<String> limpiarLineas(String texto) {
        if (texto == null) return new ArrayList<>();
        return Arrays.stream(texto.split("\\R"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private List<Integer> parsearSolucion(String texto, int totalBloques) {
        if (texto == null || texto.trim().isBlank()) {
            List<Integer> ordenNatural = new ArrayList<>();
            for (int i = 0; i < totalBloques; i++) ordenNatural.add(i);
            return ordenNatural;
        }

        String normalizado = texto.replace(";", ",").replace(" ", ",");
        List<Integer> indices = new ArrayList<>();
        for (String item : normalizado.split(",")) {
            if (item.isBlank()) continue;
            try {
                int indice = Integer.parseInt(item.trim());
                if (indice < 0 || indice >= totalBloques) {
                    throw new IllegalArgumentException("La solución contiene el índice " + indice + ", pero los bloques van de 0 a " + (totalBloques - 1) + ".");
                }
                indices.add(indice);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La solución debe usar índices numéricos separados por coma. Ejemplo: 0,1,2,3.");
            }
        }
        if (indices.isEmpty()) throw new IllegalArgumentException("Define el orden correcto de la solución.");
        return indices;
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
