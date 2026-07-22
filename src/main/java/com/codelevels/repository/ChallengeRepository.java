package com.codelevels.repository;

import com.codelevels.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    boolean existsByTitulo(String titulo);
    List<Challenge> findByPremiumFalse();
    List<Challenge> findByPremiumTrue();
}
