package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.AssignedTask;
import com.codelevels.model.ChallengeRequest;
import com.codelevels.model.Role;
import com.codelevels.model.TeacherExam;
import com.codelevels.model.ExamSubmission;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.AssignedTaskRepository;
import com.codelevels.repository.ChallengeRepository;
import com.codelevels.repository.ChallengeRequestRepository;
import com.codelevels.repository.ProgressRepository;
import com.codelevels.repository.TeacherExamRepository;
import com.codelevels.repository.ExamSubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/docente")
@RequiredArgsConstructor
public class DocenteController {
    private final AppUserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ProgressRepository progressRepository;
    private final AssignedTaskRepository assignedTaskRepository;
    private final TeacherExamRepository teacherExamRepository;
    private final ChallengeRequestRepository challengeRequestRepository;
    private final ExamSubmissionRepository examSubmissionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        AppUser docente = userRepository.findByEmail(auth.getName()).orElseThrow();
        var usuarios = userRepository.findAll();
        var alumnosLista = usuarios.stream().filter(u -> u.getRol() == Role.ALUMNO).toList();
        long docentes = usuarios.stream().filter(u -> u.getRol() == Role.DOCENTE).count();

        model.addAttribute("alumnos", alumnosLista.size());
        model.addAttribute("docentes", docentes);
        model.addAttribute("retos", challengeRepository.findAll());
        model.addAttribute("usuarios", alumnosLista);
        model.addAttribute("progresos", progressRepository.findAll());
        model.addAttribute("totalRetos", challengeRepository.count());
        model.addAttribute("retosCompletados", progressRepository.countCompleted());
        model.addAttribute("tareas", assignedTaskRepository.findByDocenteOrderByCreadoEnDesc(docente));
        model.addAttribute("examenesDocente", teacherExamRepository.findByDocenteOrderByCreadoEnDesc(docente));
        model.addAttribute("solicitudes", challengeRequestRepository.findByDocenteOrderByCreadoEnDesc(docente));
        model.addAttribute("totalTareas", assignedTaskRepository.findByDocenteOrderByCreadoEnDesc(docente).size());
        model.addAttribute("totalExamenes", teacherExamRepository.findByDocenteOrderByCreadoEnDesc(docente).size());
        model.addAttribute("totalSolicitudes", challengeRequestRepository.findByDocenteOrderByCreadoEnDesc(docente).size());
        model.addAttribute("entregasExamen", examSubmissionRepository.findByDocenteOrderByEnviadoEnDesc(docente));
        model.addAttribute("examenesPendientesRevision", examSubmissionRepository.countByDocenteAndEstado(docente, "PENDIENTE"));
        return "docente/dashboard";
    }

    @PostMapping("/tareas")
    public String crearTarea(@RequestParam String titulo,
                             @RequestParam String descripcion,
                             @RequestParam String dificultad,
                             @RequestParam String tema,
                             @RequestParam(required = false) Long alumnoId,
                             @RequestParam(required = false) String fechaEntrega,
                             Authentication auth,
                             RedirectAttributes redirectAttributes) {
        try {
            AppUser docente = userRepository.findByEmail(auth.getName()).orElseThrow();
            AppUser alumno = null;
            if (alumnoId != null && alumnoId > 0) {
                alumno = userRepository.findById(alumnoId).filter(u -> u.getRol() == Role.ALUMNO).orElse(null);
            }

            if (titulo.trim().isBlank() || descripcion.trim().isBlank() || tema.trim().isBlank()) {
                redirectAttributes.addFlashAttribute("errorTarea", "Completa título, descripción y tema.");
                return "redirect:/docente/dashboard#tareas";
            }

            AssignedTask tarea = AssignedTask.builder()
                    .titulo(titulo.trim())
                    .descripcion(descripcion.trim())
                    .dificultad(dificultad.trim())
                    .tema(tema.trim())
                    .fechaEntrega(fechaEntrega == null || fechaEntrega.isBlank() ? null : LocalDate.parse(fechaEntrega))
                    .docente(docente)
                    .alumno(alumno)
                    .build();
            assignedTaskRepository.save(tarea);
            redirectAttributes.addFlashAttribute("okTarea", "Tarea asignada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorTarea", "No se pudo asignar la tarea. Revisa los datos.");
        }
        return "redirect:/docente/dashboard#tareas";
    }

    @PostMapping("/examenes")
    public String crearExamen(@RequestParam String titulo,
                              @RequestParam String dificultad,
                              @RequestParam String tema,
                              @RequestParam String descripcion,
                              @RequestParam(defaultValue = "30") Integer tiempoMinutos,
                              @RequestParam String preguntas,
                              @RequestParam(required = false) MultipartFile archivoExamen,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        try {
            AppUser docente = userRepository.findByEmail(auth.getName()).orElseThrow();
            List<String> preguntasList = limpiarLineas(preguntas);
            if (titulo.trim().isBlank() || descripcion.trim().isBlank() || tema.trim().isBlank() || preguntasList.size() < 3) {
                redirectAttributes.addFlashAttribute("errorExamen", "Completa los datos y agrega al menos 3 preguntas.");
                return "redirect:/docente/dashboard#crear-examen";
            }

            String archivoNombre = "";
            String archivoUrl = "";
            String tipoArchivo = "";
            if (archivoExamen != null && !archivoExamen.isEmpty()) {
                StoredFile storedFile = guardarArchivo(archivoExamen, "examenes", List.of("pdf", "png", "jpg", "jpeg"));
                archivoNombre = storedFile.originalName();
                archivoUrl = storedFile.publicUrl();
                tipoArchivo = storedFile.extension();
            }

            TeacherExam examen = TeacherExam.builder()
                    .titulo(titulo.trim())
                    .dificultad(dificultad.trim())
                    .tema(tema.trim())
                    .descripcion(descripcion.trim())
                    .tiempoMinutos(Math.max(tiempoMinutos, 5))
                    .preguntasJson(toJson(preguntasList))
                    .archivoNombre(archivoNombre)
                    .archivoUrl(archivoUrl)
                    .tipoArchivo(tipoArchivo)
                    .docente(docente)
                    .publicado(true)
                    .build();
            teacherExamRepository.save(examen);
            redirectAttributes.addFlashAttribute("okExamen", "Examen creado y publicado para los alumnos.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorExamen", "No se pudo crear el examen. Revisa las preguntas o el archivo adjunto.");
        }
        return "redirect:/docente/dashboard#examenes";
    }

    @PostMapping("/solicitudes-retos")
    public String solicitarReto(@RequestParam String titulo,
                                @RequestParam String dificultad,
                                @RequestParam String categoria,
                                @RequestParam String descripcion,
                                @RequestParam(required = false, defaultValue = "") String justificacion,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            AppUser docente = userRepository.findByEmail(auth.getName()).orElseThrow();
            if (titulo.trim().isBlank() || categoria.trim().isBlank() || descripcion.trim().isBlank()) {
                redirectAttributes.addFlashAttribute("errorSolicitud", "Completa título, categoría y descripción.");
                return "redirect:/docente/dashboard#solicitar-reto";
            }
            ChallengeRequest solicitud = ChallengeRequest.builder()
                    .titulo(titulo.trim())
                    .dificultad(dificultad.trim())
                    .categoria(categoria.trim())
                    .descripcion(descripcion.trim())
                    .justificacion(justificacion.trim())
                    .docente(docente)
                    .build();
            challengeRequestRepository.save(solicitud);
            redirectAttributes.addFlashAttribute("okSolicitud", "Solicitud enviada al administrador.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorSolicitud", "No se pudo enviar la solicitud.");
        }
        return "redirect:/docente/dashboard#solicitudes";
    }

    @PostMapping("/entregas-examen/{id}/calificar")
    public String calificarEntrega(@org.springframework.web.bind.annotation.PathVariable Long id,
                                   @RequestParam Integer calificacion,
                                   @RequestParam(required = false, defaultValue = "") String retroalimentacion,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            AppUser docente = userRepository.findByEmail(auth.getName()).orElseThrow();
            ExamSubmission entrega = examSubmissionRepository.findById(id).orElseThrow();
            if (!entrega.getDocente().getId().equals(docente.getId())) {
                redirectAttributes.addFlashAttribute("errorRevision", "No puedes calificar una entrega de otro docente.");
                return "redirect:/docente/dashboard#revisar-examenes";
            }
            int nota = Math.max(0, Math.min(20, calificacion));
            entrega.setCalificacion(nota);
            entrega.setRetroalimentacion(retroalimentacion == null ? "" : retroalimentacion.trim());
            entrega.setEstado("CALIFICADO");
            entrega.setNotificadoAlumno(true);
            entrega.setCalificadoEn(LocalDateTime.now());
            examSubmissionRepository.save(entrega);
            redirectAttributes.addFlashAttribute("okRevision", "Examen calificado. El alumno verá la notificación en su panel.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorRevision", "No se pudo calificar la entrega.");
        }
        return "redirect:/docente/dashboard#revisar-examenes";
    }

    private List<String> limpiarLineas(String texto) {
        if (texto == null) return List.of();
        return Arrays.stream(texto.split("\\R"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private StoredFile guardarArchivo(MultipartFile file, String folder, List<String> allowedExtensions) throws IOException {
        String original = file.getOriginalFilename() == null ? "archivo" : file.getOriginalFilename();
        String cleanName = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String extension = "";
        int dot = cleanName.lastIndexOf('.');
        if (dot >= 0 && dot < cleanName.length() - 1) {
            extension = cleanName.substring(dot + 1).toLowerCase();
        }
        if (!allowedExtensions.contains(extension)) {
            throw new IOException("Tipo de archivo no permitido");
        }
        Path dir = Paths.get("uploads", folder);
        Files.createDirectories(dir);
        String stored = UUID.randomUUID() + "_" + cleanName;
        Path target = dir.resolve(stored);
        file.transferTo(target.toFile());
        return new StoredFile(original, "/uploads/" + folder + "/" + stored, extension);
    }

    private record StoredFile(String originalName, String publicUrl, String extension) {}

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
