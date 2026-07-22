package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.Challenge;
import com.codelevels.model.Progress;
import com.codelevels.model.ExamSubmission;
import com.codelevels.model.TeacherExam;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.AssignedTaskRepository;
import com.codelevels.repository.TeacherExamRepository;
import com.codelevels.repository.ChallengeRepository;
import com.codelevels.repository.ProgressRepository;
import com.codelevels.repository.ExamSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/alumno")
@RequiredArgsConstructor
public class AlumnoController {
    private final ChallengeRepository challengeRepository;
    private final ProgressRepository progressRepository;
    private final AppUserRepository userRepository;
    private final AssignedTaskRepository assignedTaskRepository;
    private final TeacherExamRepository teacherExamRepository;
    private final ExamSubmissionRepository examSubmissionRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        addStudentStats(model, auth);
        model.addAttribute("retos", challengeRepository.findAll());
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("tareasAsignadas", assignedTaskRepository.findByAlumnoOrAlumnoIsNullOrderByCreadoEnDesc(user));
        model.addAttribute("examenesDocente", teacherExamRepository.findByPublicadoTrueOrderByCreadoEnDesc());
        model.addAttribute("entregasExamen", examSubmissionRepository.findByAlumnoOrderByEnviadoEnDesc(user));
        model.addAttribute("notificacionesExamen", examSubmissionRepository.countByAlumnoAndEstado(user, "CALIFICADO"));
        return "alumno/dashboard";
    }

    @GetMapping("/ruta")
    public String ruta(Model model, Authentication auth) {
        addStudentStats(model, auth);
        model.addAttribute("retos", challengeRepository.findAll());
        return "alumno/ruta";
    }

    @GetMapping("/retos")
    public String retos(Model model, Authentication auth) {
        addStudentStats(model, auth);
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("esPremium", user.isPremium());
        model.addAttribute("retos", challengeRepository.findAll());
        return "alumno/retos";
    }

    @GetMapping("/laboratorio")
    public String laboratorio(Model model, Authentication auth) {
        addStudentStats(model, auth);
        return "alumno/laboratorio";
    }

    @GetMapping("/tutor")
    public String tutor(Model model, Authentication auth) {
        addStudentStats(model, auth);
        return "alumno/tutor";
    }

    @GetMapping("/progreso")
    public String progreso(Model model, Authentication auth) {
        addStudentStats(model, auth);
        return "alumno/progreso";
    }

    @GetMapping("/examenes")
    public String examenes(Model model, Authentication auth) {
        addStudentStats(model, auth);
        model.addAttribute("retos", challengeRepository.findAll());
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("examenesDocente", teacherExamRepository.findByPublicadoTrueOrderByCreadoEnDesc());
        model.addAttribute("entregasExamen", examSubmissionRepository.findByAlumnoOrderByEnviadoEnDesc(user));
        model.addAttribute("notificacionesExamen", examSubmissionRepository.countByAlumnoAndEstado(user, "CALIFICADO"));
        return "alumno/examenes";
    }


    @PostMapping("/examenes/{id}/entregar")
    public String entregarExamen(@PathVariable Long id,
                                 @RequestParam(required = false, defaultValue = "") String respuestaTexto,
                                 @RequestParam(required = false) MultipartFile archivoRespuesta,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        try {
            AppUser alumno = userRepository.findByEmail(auth.getName()).orElseThrow();
            TeacherExam examen = teacherExamRepository.findById(id).orElseThrow();

            if ((respuestaTexto == null || respuestaTexto.trim().isBlank()) && (archivoRespuesta == null || archivoRespuesta.isEmpty())) {
                redirectAttributes.addFlashAttribute("errorEntrega", "Escribe tu respuesta o sube tu archivo resuelto en Word o PDF.");
                return "redirect:/alumno/examenes#examenes-docente";
            }

            String archivoNombre = "";
            String archivoUrl = "";
            if (archivoRespuesta != null && !archivoRespuesta.isEmpty()) {
                StoredFile storedFile = guardarArchivo(archivoRespuesta, "respuestas", List.of("pdf", "doc", "docx"));
                archivoNombre = storedFile.originalName();
                archivoUrl = storedFile.publicUrl();
            }

            ExamSubmission entrega = examSubmissionRepository.findByAlumnoAndExamen(alumno, examen)
                    .orElseGet(() -> ExamSubmission.builder().alumno(alumno).examen(examen).docente(examen.getDocente()).build());
            entrega.setRespuestaTexto(respuestaTexto == null ? "" : respuestaTexto.trim());
            entrega.setArchivoNombre(archivoNombre);
            entrega.setArchivoUrl(archivoUrl);
            entrega.setEstado("PENDIENTE");
            entrega.setCalificacion(null);
            entrega.setRetroalimentacion("");
            entrega.setNotificadoAlumno(false);
            entrega.setCalificadoEn(null);
            examSubmissionRepository.save(entrega);
            redirectAttributes.addFlashAttribute("okEntrega", "Examen enviado al docente para revisión.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorEntrega", "No se pudo enviar el examen. Revisa el archivo Word/PDF.");
        }
        return "redirect:/alumno/examenes#mis-entregas";
    }

    private void addStudentStats(Model model, Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<Challenge> retos = challengeRepository.findAll();
        List<Progress> progreso = progressRepository.findByUsuario(user);

        long totalRetos = retos.size();
        long completados = progreso.stream().filter(Progress::isCompletado).count();
        int puntos = progreso.stream().mapToInt(Progress::getPuntos).sum();
        int intentos = progreso.stream().mapToInt(Progress::getIntentos).sum();
        int precision = progreso.isEmpty() ? 0 : (int) Math.round(progreso.stream().mapToInt(Progress::getPrecision).average().orElse(0));
        int porcentaje = totalRetos == 0 ? 0 : (int) Math.round((completados * 100.0) / totalRetos);
        int nivel = puntos < 100 ? 1 : puntos < 300 ? 2 : puntos < 600 ? 3 : puntos < 1000 ? 4 : 5;

        model.addAttribute("user", user);
        model.addAttribute("esPremium", user.isPremium());
        model.addAttribute("totalRetos", totalRetos);
        model.addAttribute("completados", completados);
        model.addAttribute("pendientes", Math.max(totalRetos - completados, 0));
        model.addAttribute("puntos", puntos);
        model.addAttribute("intentos", intentos);
        model.addAttribute("precision", precision);
        model.addAttribute("porcentaje", porcentaje);
        model.addAttribute("nivel", nivel);
        model.addAttribute("progreso", progreso);
        model.addAttribute("retos", retos);
        model.addAttribute("tareasAsignadas", assignedTaskRepository.findByAlumnoOrAlumnoIsNullOrderByCreadoEnDesc(user));
        model.addAttribute("examenesDocente", teacherExamRepository.findByPublicadoTrueOrderByCreadoEnDesc());
        model.addAttribute("entregasExamen", examSubmissionRepository.findByAlumnoOrderByEnviadoEnDesc(user));
        model.addAttribute("notificacionesExamen", examSubmissionRepository.countByAlumnoAndEstado(user, "CALIFICADO"));
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
}

