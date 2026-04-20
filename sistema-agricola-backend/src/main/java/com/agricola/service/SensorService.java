package com.agricola.service;

import com.agricola.model.DatosSensor;
import com.agricola.model.Sensor;
import com.agricola.repository.DatosSensorRepository;
import com.agricola.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final DatosSensorRepository datosSensorRepository;

    public Sensor obtenerSensor(String sensorId) {
        return sensorRepository.findById(sensorId)
            .orElseThrow(() -> new RuntimeException("Sensor no encontrado: " + sensorId));
    }

    public List<Sensor> obtenerSensoresDeCampo(String idCampo) {
        return sensorRepository.findByCampoIdCampo(idCampo);
    }

    public DatosSensor obtenerUltimoDato(String sensorId) {
        return datosSensorRepository.findLatestBySensorId(sensorId)
            .orElseThrow(() -> new RuntimeException("Sin datos para sensor: " + sensorId));
    }

    public DatosSensor guardarDatos(Map<String, Object> datos) {
        String sensorId = (String) datos.get("sensorId");
        Sensor sensor = obtenerSensor(sensorId);

        DatosSensor dato = DatosSensor.builder()
            .sensor(sensor)
            .temperatura(toDouble(datos.get("temperatura")))
            .humedad(toDouble(datos.get("humedad")))
            .humedadSuelo(toDouble(datos.get("humedad_suelo")))
            .fecha(LocalDateTime.now())
            .build();

        return datosSensorRepository.save(dato);
    }

    public Map<String, Object> obtenerHistorial(String idCampo, int diasAnteriores) {
        LocalDateTime desde = LocalDateTime.now().minusDays(diasAnteriores);
        List<DatosSensor> datos = datosSensorRepository.findByCampoAndFechaAfter(idCampo, desde);

        if (datos.isEmpty()) {
            return buildHistorialVacio(idCampo, diasAnteriores);
        }

        double tempAvg = datos.stream().mapToDouble(d -> d.getTemperatura() != null ? d.getTemperatura() : 0).average().orElse(0);
        double humAvg  = datos.stream().mapToDouble(d -> d.getHumedad() != null     ? d.getHumedad()     : 0).average().orElse(0);
        double suelAvg = datos.stream().mapToDouble(d -> d.getHumedadSuelo() != null ? d.getHumedadSuelo() : 0).average().orElse(0);

        Map<String, Object> historial = new LinkedHashMap<>();
        historial.put("idCampo", idCampo);
        historial.put("fecha_inicio", desde);
        historial.put("fecha_fin", LocalDateTime.now());
        historial.put("datos_promedio", Map.of(
            "temperatura_avg", Math.round(tempAvg * 10.0) / 10.0,
            "humedad_avg",     Math.round(humAvg  * 10.0) / 10.0,
            "humedad_suelo_avg", Math.round(suelAvg * 10.0) / 10.0
        ));
        historial.put("tendencias", Map.of(
            "temperatura", "estable",
            "humedad", "estable"
        ));
        return historial;
    }

    private Map<String, Object> buildHistorialVacio(String idCampo, int dias) {
        return Map.of(
            "idCampo", idCampo,
            "fecha_inicio", LocalDateTime.now().minusDays(dias),
            "fecha_fin", LocalDateTime.now(),
            "datos_promedio", Map.of("temperatura_avg", 0, "humedad_avg", 0, "humedad_suelo_avg", 0),
            "tendencias", Map.of("temperatura", "estable", "humedad", "estable")
        );
    }

    private Double toDouble(Object val) {
        if (val == null) return null;
        if (val instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return null; }
    }
}
