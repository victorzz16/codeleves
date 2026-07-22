package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.PremiumPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PremiumPaymentRepository extends JpaRepository<PremiumPayment, Long> {
    List<PremiumPayment> findByUsuarioOrderByCreadoEnDesc(AppUser usuario);
}
