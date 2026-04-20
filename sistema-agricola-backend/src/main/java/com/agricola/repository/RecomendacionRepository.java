package com.agricola.repository;

import com.agricola.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, String> {
    List<Recomendacion> findByIdCampoOrderByFechaGeneradaDesc(String idCampo);
    List<Recomendacion> findByIdCampoAndTipo(String idCampo, Recomendacion.TipoRecomendacion tipo);
}
