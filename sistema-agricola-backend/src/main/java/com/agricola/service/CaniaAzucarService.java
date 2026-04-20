package com.agricola.service;

import com.agricola.model.*;
import com.agricola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio especializado en caña de azúcar
 * Lógica agronómica basada en constantes-cana-azucar.ts y servicio-cana-azucar.ts
 */
@Service
@RequiredArgsConstructor
public class CaniaAzucarService {

    private final CampoRepository campoRepository;
    private final MetricasCanaRepository metricasCanaRepository;
    private final CicloHistorialRepository cicloHistorialRepository;
    private final DatosSensorRepository datosSensorRepository;

    // ===== MÉTRICAS ACTUALES =====
    public Map<String, Object> obtenerMetricas(String idCampo) {
        Optional<MetricasCana> metricas = metricasCanaRepository.findLatestByIdCampo(idCampo);

        if (metricas.isPresent()) {
            return mapMetricas(metricas.get());
        }

        // Generar métricas simuladas realistas si no hay registro
        return generarMetricasSimuladas(idCampo);
    }

    // ===== PREDICCIÓN CAÑA =====
    public Map<String, Object> obtenerPrediccion(String idCampo) {
        Campo campo = obtenerCampo(idCampo);
        Map<String, Object> metricas = obtenerMetricas(idCampo);

        int diasDesdeEmergencia = campo.getFechaSiembra() != null
            ? (int) java.time.temporal.ChronoUnit.DAYS.between(campo.getFechaSiembra(), LocalDate.now())
            : 250;

        double brix = toDouble(metricas.get("brix"), 16.0);
        double pol  = toDouble(metricas.get("pol"),  13.0);
        double temp = toDouble(metricas.get("temperatura"), 25.0);
        double humSuelo = toDouble(metricas.get("humedad_suelo"), 68.0);

        double toneladas   = calcularRendimiento(diasDesdeEmergencia, temp, humSuelo);
        double azucarKgTon = (pol / 100.0) * 10 * 0.75 * 1000; // kg azúcar por ton caña

        Map<String, Object> prediccion = new LinkedHashMap<>();
        prediccion.put("idCampo", idCampo);
        prediccion.put("brix_esperado", brix);
        prediccion.put("pol_esperado", pol);
        prediccion.put("toneladas_esperadas", toneladas);
        prediccion.put("azucar_estimada", Math.round(azucarKgTon * 10.0) / 10.0);
        prediccion.put("dias_para_cosecha", calcularDiasParaCosecha(campo, diasDesdeEmergencia));
        prediccion.put("rendimiento_esperado", toneladas * 1000);
        prediccion.put("confianza", 85.0);
        prediccion.put("fecha_prediccion", LocalDateTime.now());
        prediccion.put("fecha_cosecha_estimada", LocalDate.now().plusDays(calcularDiasParaCosecha(campo, diasDesdeEmergencia)));
        prediccion.put("factores", Map.of("clima", temp * 2.5, "suelo", humSuelo * 0.8, "agua", humSuelo, "plagas", 15.0));
        return prediccion;
    }

    // ===== ANÁLISIS PLAGAS CAÑA =====
    public Map<String, Object> obtenerPlagas(String idCampo) {
        Map<String, Object> metricas = obtenerMetricas(idCampo);
        double temp   = toDouble(metricas.get("temperatura"), 25.0);
        double humHoja = toDouble(metricas.get("humedad_hoja"), 70.0);

        Map<String, Object> plagasDetectadas = new LinkedHashMap<>();
        plagasDetectadas.put("chicharra",        calcularRiesgoPlaga(temp, humHoja, 22, 30, 70, 90));
        plagasDetectadas.put("minador",          calcularRiesgoPlaga(temp, humHoja, 20, 28, 65, 85));
        plagasDetectadas.put("gusano_cogollero", calcularRiesgoPlaga(temp, humHoja, 24, 32, 60, 80));
        plagasDetectadas.put("gusano_pegador",   calcularRiesgoPlaga(temp, humHoja, 22, 30, 65, 85));
        plagasDetectadas.put("nematodo_tallo",   calcularRiesgoPlaga(temp, humHoja, 18, 28, 70, 90));
        plagasDetectadas.put("hongos_foliares",  calcularRiesgoPlaga(temp, humHoja, 20, 28, 80, 95));
        plagasDetectadas.put("carbunclo",        calcularRiesgoPlaga(temp, humHoja, 20, 28, 75, 95));
        plagasDetectadas.put("pokkah_boeng",     calcularRiesgoPlaga(temp, humHoja, 22, 28, 80, 95));

        double maxRiesgo = plagasDetectadas.values().stream()
            .mapToDouble(v -> ((Number) v).doubleValue()).max().orElse(0);
        boolean nivelCritico = maxRiesgo > 75;

        List<String> recs = nivelCritico
            ? List.of("Aplicar control químico urgente", "Consultar agrónomo inmediatamente",
                      "Tomar muestras para identificación de plaga")
            : List.of("Continuar monitoreo semanal", "Registrar observaciones en campo");

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("idCampo", idCampo);
        resultado.put("plagas_detectadas", plagasDetectadas);
        resultado.put("nivel_critico", nivelCritico);
        resultado.put("recomendaciones_plagas", recs);
        resultado.put("fecha_deteccion", LocalDateTime.now());
        return resultado;
    }

    // ===== ANÁLISIS SUELO =====
    public Map<String, Object> obtenerAnalisisSuelo(String idCampo) {
        Map<String, Object> suelo = new LinkedHashMap<>();
        suelo.put("idCampo", idCampo);
        suelo.put("nitrogeno", 95.0);
        suelo.put("fosforo",   55.0);
        suelo.put("potasio",   220.0);
        suelo.put("calcio",    800.0);
        suelo.put("magnesio",  120.0);
        suelo.put("azufre",    40.0);
        suelo.put("micronutrientes", Map.of(
            "hierro", 45.0, "manganeso", 12.0, "zinc", 3.5, "boro", 0.8, "cobre", 2.1
        ));
        suelo.put("recomendacion_fertilizacion", Map.of(
            "nitrogeno_kg_ha", 120.0,
            "fosforo_kg_ha",   60.0,
            "potasio_kg_ha",   80.0
        ));
        return suelo;
    }

    // ===== RECOMENDACIÓN FERTILIZACIÓN =====
    public Map<String, Object> obtenerRecomendacionFertilizacion(String idCampo) {
        Campo campo = obtenerCampo(idCampo);
        boolean esPlantilla = campo.getCicloVegetativo() == null
            || campo.getCicloVegetativo() == Campo.CicloVegetativo.plantilla;

        double n = esPlantilla ? 120.0 : 100.0;
        double p = esPlantilla ? 60.0  : 40.0;
        double k = esPlantilla ? 80.0  : 60.0;

        List<LocalDate> fechas = esPlantilla
            ? List.of(LocalDate.now().plusDays(45), LocalDate.now().plusDays(90))
            : List.of(LocalDate.now().plusDays(60));

        Map<String, Object> rec = new LinkedHashMap<>();
        rec.put("nitrogeno_kg_ha", n);
        rec.put("fosforo_kg_ha",   p);
        rec.put("potasio_kg_ha",   k);
        rec.put("fechas_aplicacion", fechas);
        rec.put("descripcion", esPlantilla
            ? "Primera fertilización a los 45 días y segunda a los 90 días después de la siembra."
            : "Aplicar en dosis única a los 60 días después del corte.");
        return rec;
    }

    // ===== ESTIMACIÓN COSECHA =====
    public Map<String, Object> estimarCosecha(String idCampo) {
        Campo campo = obtenerCampo(idCampo);
        Map<String, Object> metricas = obtenerMetricas(idCampo);

        int diasDesdeEmergencia = campo.getFechaSiembra() != null
            ? (int) java.time.temporal.ChronoUnit.DAYS.between(campo.getFechaSiembra(), LocalDate.now())
            : 250;

        double temp    = toDouble(metricas.get("temperatura"), 25.0);
        double humSuelo = toDouble(metricas.get("humedad_suelo"), 68.0);
        double brix    = toDouble(metricas.get("brix"), 16.0);
        double pol     = toDouble(metricas.get("pol"),  13.0);
        int diasFaltantes = calcularDiasParaCosecha(campo, diasDesdeEmergencia);

        Map<String, Object> estimacion = new LinkedHashMap<>();
        estimacion.put("fecha_optima",        LocalDate.now().plusDays(diasFaltantes));
        estimacion.put("toneladas_estimadas", calcularRendimiento(diasDesdeEmergencia, temp, humSuelo));
        estimacion.put("brix_estimado",       brix);
        estimacion.put("pol_estimado",        pol);
        estimacion.put("calidad_azucar",      Math.round((pol / brix) * 100 * 10.0) / 10.0);
        estimacion.put("dias_faltantes",      diasFaltantes);
        return estimacion;
    }

    // ===== CALIDAD DEL JUGO =====
    public Map<String, Object> obtenerCalidadJugo(String idCampo) {
        Map<String, Object> metricas = obtenerMetricas(idCampo);
        double brix   = toDouble(metricas.get("brix"),   16.5);
        double pol    = toDouble(metricas.get("pol"),    13.5);
        double pureza = toDouble(metricas.get("pureza"), 84.0);
        double fibra  = toDouble(metricas.get("fibra"),  12.0);

        double rendAzucar = (pol / 100.0) * 0.9 * 1000;
        String comparativa = brix >= 15 && pol >= 13
            ? "Dentro del rango óptimo (Brix 15-20, Pol 13-18)"
            : "Por debajo del óptimo - revisar nutrición y madurez";

        return Map.of(
            "brix", brix, "pol", pol, "pureza", pureza,
            "fibra", fibra, "rendimiento_azucar", Math.round(rendAzucar),
            "comparativa_optimo", comparativa
        );
    }

    // ===== RECOMENDACIÓN RIEGO =====
    public Map<String, Object> obtenerRecomendacionRiego(String idCampo) {
        Campo campo = obtenerCampo(idCampo);
        Map<String, Object> metricas = obtenerMetricas(idCampo);
        double humSuelo = toDouble(metricas.get("humedad_suelo"), 68.0);

        int diasParaRiego = humSuelo < 65 ? 0 : (int) ((humSuelo - 65) / 2);
        double mmReq = humSuelo < 55 ? 40.0 : 25.0;
        String justificacion = humSuelo < 55
            ? "Humedad crítica. Riego inmediato necesario."
            : "Riego preventivo para mantener capacidad de campo óptima.";
        String etapa = campo.getCicloVegetativo() != null
            ? campo.getCicloVegetativo().name() : "plantilla";

        return Map.of(
            "proxima_fecha_riego", LocalDateTime.now().plusDays(diasParaRiego),
            "cantidad_mm", mmReq,
            "justificacion", justificacion,
            "etapa_cultivo", etapa
        );
    }

    // ===== HISTORIAL CICLOS =====
    public List<Map<String, Object>> obtenerHistorialCiclos(String idCampo) {
        List<CicloHistorial> historial = cicloHistorialRepository.findByIdCampoOrderByFechaCorteDesc(idCampo);

        if (!historial.isEmpty()) {
            return historial.stream().map(c -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("ciclo", c.getCiclo());
                m.put("fecha_corte", c.getFechaCorte());
                m.put("toneladas_cosechadas", c.getToneladasCosechadas());
                m.put("brix_promedio", c.getBrixPromedio());
                m.put("pol_promedio", c.getPolPromedio());
                m.put("incidencia_plagas", c.getIncidenciaPlagas() != null
                    ? Arrays.asList(c.getIncidenciaPlagas().split(",")) : List.of());
                return m;
            }).toList();
        }

        // Datos históricos de ejemplo
        return List.of(
            Map.of("ciclo", "plantilla", "fecha_corte", LocalDate.now().minusMonths(18),
                   "toneladas_cosechadas", 82.5, "brix_promedio", 17.2, "pol_promedio", 14.1,
                   "incidencia_plagas", List.of("carbunclo")),
            Map.of("ciclo", "resoca_1", "fecha_corte", LocalDate.now().minusMonths(6),
                   "toneladas_cosechadas", 67.0, "brix_promedio", 16.8, "pol_promedio", 13.8,
                   "incidencia_plagas", List.of())
        );
    }

    // ===== IMPACTO CLIMÁTICO =====
    public Map<String, Object> analizarImpactoClimatico(String idCampo, int dias) {
        Map<String, Object> metricas = obtenerMetricas(idCampo);
        double temp = toDouble(metricas.get("temperatura"), 25.0);
        double humSuelo = toDouble(metricas.get("humedad_suelo"), 68.0);

        double impacto = temp > 30 ? -8.5 : temp < 18 ? -12.0 : 2.5;
        List<String> recs = new ArrayList<>();
        if (temp > 30) recs.add("Aumentar frecuencia de riego ante altas temperaturas");
        if (temp < 18) recs.add("Proteger contra heladas si es posible");
        recs.add("Monitorear pronóstico cada 48 horas");

        return Map.of(
            "temperatura_promedio", temp,
            "lluvia_esperada_mm", 85.0,
            "humedad_promedio", humSuelo,
            "impacto_rendimiento", impacto,
            "recomendaciones", recs
        );
    }

    // ===== UTILIDADES PRIVADAS =====

    private Campo obtenerCampo(String idCampo) {
        return campoRepository.findById(idCampo)
            .orElseThrow(() -> new RuntimeException("Campo no encontrado: " + idCampo));
    }

    private Map<String, Object> generarMetricasSimuladas(String idCampo) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("idCampo", idCampo);
        m.put("fecha", LocalDateTime.now());
        m.put("temperatura",     25.0 + (Math.random() * 4 - 2));
        m.put("brix",            16.5 + (Math.random() * 2 - 1));
        m.put("pol",             13.5 + (Math.random() * 2 - 1));
        m.put("pureza",          84.0 + (Math.random() * 4 - 2));
        m.put("fibra",           12.0 + (Math.random() * 2 - 1));
        m.put("humedad_hoja",    70.0 + (Math.random() * 4 - 2));
        m.put("ph_suelo",        6.5  + (Math.random() * 0.5 - 0.25));
        m.put("densidad_tallos", 11.0 + (Math.random() * 2 - 1));
        m.put("altura_promedio", 180.0 + (Math.random() * 20 - 10));
        m.put("humedad_suelo",   68.0 + (Math.random() * 6 - 3));
        return m;
    }

    private Map<String, Object> mapMetricas(MetricasCana m) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("idCampo", m.getIdCampo());
        result.put("fecha", m.getFecha());
        result.put("temperatura",     m.getTemperatura());
        result.put("brix",            m.getBrix());
        result.put("pol",             m.getPol());
        result.put("pureza",          m.getPureza());
        result.put("fibra",           m.getFibra());
        result.put("humedad_hoja",    m.getHumedadHoja());
        result.put("ph_suelo",        m.getPhSuelo());
        result.put("densidad_tallos", m.getDensidadTallos());
        result.put("altura_promedio", m.getAlturaPromedio());
        result.put("humedad_suelo",   m.getHumedadSuelo());
        return result;
    }

    private double calcularRendimiento(int diasDesdeEmergencia, double temp, double humSuelo) {
        double rendimiento = 65.0;
        double edadFactor  = Math.min(diasDesdeEmergencia / 500.0, 1.0);
        rendimiento *= edadFactor;
        double tempFactor  = (temp >= 20 && temp <= 30) ? 1.0 : 0.8;
        rendimiento *= tempFactor;
        double humFactor   = (humSuelo >= 65 && humSuelo <= 75) ? 1.0 : 0.85;
        rendimiento *= humFactor;
        return Math.round(rendimiento * 10.0) / 10.0;
    }

    private int calcularDiasParaCosecha(Campo campo, int diasActuales) {
        int diasTotales = switch (campo.getCicloVegetativo() != null
            ? campo.getCicloVegetativo() : Campo.CicloVegetativo.plantilla) {
            case plantilla -> 540;
            default        -> 360;
        };
        return Math.max(0, diasTotales - diasActuales);
    }

    private double calcularRiesgoPlaga(double temp, double hum,
                                       double tMin, double tMax,
                                       double hMin, double hMax) {
        boolean condTemp = temp >= tMin && temp <= tMax;
        boolean condHum  = hum  >= hMin && hum  <= hMax;
        if (condTemp && condHum)  return 55.0 + Math.random() * 25;
        if (condTemp || condHum)  return 25.0 + Math.random() * 20;
        return 5.0 + Math.random() * 15;
    }

    private double toDouble(Object val, double defaultVal) {
        if (val == null) return defaultVal;
        try { return ((Number) val).doubleValue(); }
        catch (Exception e) { return defaultVal; }
    }
}
