package com.agricola.repository;

import com.agricola.model.DatosSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DatosSensorRepository extends JpaRepository<DatosSensor, Long> {

    List<DatosSensor> findBySensorSensorIdOrderByFechaDesc(String sensorId);

    @Query("SELECT d FROM DatosSensor d WHERE d.sensor.campo.idCampo = :idCampo AND d.fecha >= :desde ORDER BY d.fecha DESC")
    List<DatosSensor> findByCampoAndFechaAfter(@Param("idCampo") String idCampo, @Param("desde") LocalDateTime desde);

    @Query("SELECT d FROM DatosSensor d WHERE d.sensor.sensorId = :sensorId ORDER BY d.fecha DESC LIMIT 1")
    Optional<DatosSensor> findLatestBySensorId(@Param("sensorId") String sensorId);
}
