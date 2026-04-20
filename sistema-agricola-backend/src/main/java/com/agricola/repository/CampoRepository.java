package com.agricola.repository;

import com.agricola.model.Campo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampoRepository extends JpaRepository<Campo, String> {
    List<Campo> findByCultivo(Campo.TipoCultivo cultivo);
}
