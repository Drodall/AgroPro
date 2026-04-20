package com.agricola.repository;

import com.agricola.model.MetricasCana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface MetricasCanaRepository extends JpaRepository<MetricasCana, Long> {
    @Query("SELECT m FROM MetricasCana m WHERE m.idCampo = :idCampo ORDER BY m.fecha DESC LIMIT 1")
    Optional<MetricasCana> findLatestByIdCampo(@Param("idCampo") String idCampo);
}
