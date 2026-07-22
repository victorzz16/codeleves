package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.ChallengeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRequestRepository extends JpaRepository<ChallengeRequest, Long> {
    long countByEstado(String estado);
    List<ChallengeRequest> findByDocenteOrderByCreadoEnDesc(AppUser docente);
    List<ChallengeRequest> findAllByOrderByCreadoEnDesc();
}
