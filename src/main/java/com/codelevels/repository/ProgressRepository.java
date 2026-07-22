package com.codelevels.repository;

import com.codelevels.model.AppUser;
import com.codelevels.model.Challenge;
import com.codelevels.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUsuario(AppUser usuario);
    Optional<Progress> findByUsuarioAndReto(AppUser usuario, Challenge reto);

    @Query("select count(p) from Progress p where p.completado = true")
    long countCompleted();
}
