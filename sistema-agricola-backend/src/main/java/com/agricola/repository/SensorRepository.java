package com.agricola.repository;

import com.agricola.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    List<Sensor> findByCampoIdCampo(String idCampo);
}
