package com.agricola.repository;

import com.agricola.model.CicloHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CicloHistorialRepository extends JpaRepository<CicloHistorial, Long> {
    List<CicloHistorial> findByIdCampoOrderByFechaCorteDesc(String idCampo);
}
