package com.codelevels.controller;

import com.codelevels.model.AppUser;
import com.codelevels.model.PremiumPayment;
import com.codelevels.repository.AppUserRepository;
import com.codelevels.repository.PremiumPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PremiumController {
    private static final BigDecimal PRECIO = new BigDecimal("30.00");
    private static final Set<String> METODOS = Set.of("YAPE", "PLIN", "PAGO_EFECTIVO", "TARJETA");

    private final AppUserRepository userRepository;
    private final PremiumPaymentRepository paymentRepository;

    @GetMapping("/alumno/premium")
    public String premium(Model model, Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("precioPremium", PRECIO);
        model.addAttribute("pagos", paymentRepository.findByUsuarioOrderByCreadoEnDesc(user));
        return "alumno/premium";
    }

    @PostMapping("/api/pagos/simular")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> simularPago(@RequestBody Map<String, String> payload,
                                                            Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        String metodo = payload.getOrDefault("metodo", "").trim().toUpperCase();
        if (!METODOS.contains(metodo)) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "mensaje", "Selecciona un método de pago válido."));
        }

        String operacion = "CL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime ahora = LocalDateTime.now();
        PremiumPayment payment = PremiumPayment.builder()
                .usuario(user)
                .monto(PRECIO)
                .metodo(metodo)
                .operacion(operacion)
                .estado("CONFIRMADO")
                .creadoEn(ahora)
                .build();
        paymentRepository.save(payment);

        user.setPremium(true);
        user.setPremiumDesde(ahora);
        user.setPremiumHasta(ahora.plusMonths(1));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Pago confirmado. Ya eres Premium.",
                "operacion", operacion,
                "vigencia", user.getPremiumHasta().toLocalDate().toString()
        ));
    }
}
