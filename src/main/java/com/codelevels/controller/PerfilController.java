package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.Role;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.ChallengeRepository;
import com.codelevels.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final AppUserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ProgressRepository progressRepository;

    @GetMapping
    public String perfil(Model model, Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        long totalRetos = challengeRepository.count();
        long completados = user.getRol() == Role.ALUMNO
                ? progressRepository.findByUsuario(user).stream().filter(p -> p.isCompletado()).count()
                : progressRepository.countCompleted();
        int puntos = user.getRol() == Role.ALUMNO
                ? progressRepository.findByUsuario(user).stream().mapToInt(p -> p.getPuntos()).sum()
                : (int) progressRepository.findAll().stream().mapToInt(p -> p.getPuntos()).sum();
        long usuarios = userRepository.count();
        long alumnos = userRepository.findAll().stream().filter(u -> u.getRol() == Role.ALUMNO).count();

        model.addAttribute("user", user);
        model.addAttribute("totalRetos", totalRetos);
        model.addAttribute("completados", completados);
        model.addAttribute("puntos", puntos);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("retorno", dashboardPorRol(user.getRol()));
        model.addAttribute("rolLabel", labelPorRol(user.getRol()));
        return "perfil/perfil";
    }

    @PostMapping
    public String actualizarPerfil(@RequestParam String nombre,
                                   @RequestParam(required = false) String descripcion,
                                   @RequestParam(required = false) String carrera,
                                   @RequestParam(required = false) String sede,
                                   @RequestParam(required = false) String fotoUrl,
                                   @RequestParam(required = false) String portadaUrl,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        user.setNombre(limpiar(nombre, 120));
        user.setDescripcion(limpiar(descripcion, 700));
        user.setCarrera(limpiar(carrera, 80));
        user.setSede(limpiar(sede, 80));
        user.setFotoUrl(limpiar(fotoUrl, 500));
        user.setPortadaUrl(limpiar(portadaUrl, 500));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("okPerfil", "Tu perfil se actualizó correctamente.");
        return "redirect:/perfil";
    }

    private String limpiar(String valor, int max) {
        if (valor == null) return "";
        String limpio = valor.trim();
        return limpio.length() <= max ? limpio : limpio.substring(0, max);
    }

    private String dashboardPorRol(Role rol) {
        if (rol == Role.ADMIN) return "/admin/dashboard";
        if (rol == Role.DOCENTE) return "/docente/dashboard";
        return "/alumno/dashboard";
    }

    private String labelPorRol(Role rol) {
        if (rol == Role.ADMIN) return "Administrador";
        if (rol == Role.DOCENTE) return "Docente";
        return "Alumno";
    }
}
