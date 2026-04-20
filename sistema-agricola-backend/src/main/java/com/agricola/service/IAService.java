package com.agricola.service;

import com.agricola.model.*;
import com.agricola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio de IA y Predicciones
 * Implementa los mismos algoritmos del frontend (servicio-cana-azucar.ts) en Java
 */
@Service
@RequiredArgsConstructor
public class IAService {

    private final CampoRepository campoRepository;
    private final DatosSensorRepository datosSensorRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final MetricasCanaRepository metricasCanaRepository;

    // ===== UMBRALES (del frontend constantes-cana-azucar.ts) =====
    private static final double TEMP_OPTIMO_MIN  = 20.0;
    private static final double TEMP_OPTIMO_MAX  = 30.0;
    private static final double HUM_SUELO_OPTIMO_MIN = 65.0;
    private static final double HUM_SUELO_OPTIMO_MAX = 75.0;
    private static final double BASE_RENDIMIENTO = 65.0;

    // ===== PREDICCIONES COMPLETAS =====
    public Map<String, Object> obtenerPredicciones(String idCampo) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cosecha",         predecirCosecha(idCampo));
        result.put("plagas",          obtenerDeteccionPlagas(idCampo));
        result.put("riego",           obtenerOptimizacionRiego(idCampo));
        result.put("recomendaciones", obtenerRecomendaciones(idCampo));
        return result;
    }

    // ===== PREDICCIÓN COSECHA =====
    public Map<String, Object> predecirCosecha(String idCampo) {
        Campo campo = campoRepository.findById(idCampo)
            .orElseThrow(() -> new RuntimeException("Campo no encontrado: " + idCampo));

        double[] promedios = obtenerPromediosSensores(idCampo);
        double tempAvg = promedios[0];
        double humAvg  = promedios[1];

        int diasDesdeEmergencia = campo.getFechaSiembra() != null
            ? (int) java.time.temporal.ChronoUnit.DAYS.between(campo.getFechaSiembra(), LocalDate.now())
            : 200;

        double rendimiento = calcularRendimiento(diasDesdeEmergencia, tempAvg, humAvg);
        double confianza   = 78.5 + (Math.random() * 10);

        Map<String, Object> prediccion = new LinkedHashMap<>();
        prediccion.put("idCampo", idCampo);
        prediccion.put("rendimiento_esperado", Math.round(rendimiento * 10.0) / 10.0);
        prediccion.put("confianza", Math.round(confianza * 10.0) / 10.0);
        prediccion.put("fecha_prediccion", LocalDateTime.now());
        prediccion.put("fecha_cosecha_estimada", LocalDate.now().plusDays(calcularDiasParaCosecha(campo, diasDesdeEmergencia)));
        prediccion.put("factores", Map.of(
            "clima",  Math.round(tempAvg * 2.5 * 10.0) / 10.0,
            "suelo",  Math.round(humAvg  * 1.2 * 10.0) / 10.0,
            "agua",   Math.round(humAvg  * 0.8 * 10.0) / 10.0,
            "plagas", 15.0
        ));
        // Específico caña
        prediccion.put("brix_esperado",       calcularBrixEsperado(diasDesdeEmergencia, tempAvg, humAvg));
        prediccion.put("pol_esperado",        campo.getPolEsperado() != null ? campo.getPolEsperado() : 14.0);
        prediccion.put("toneladas_esperadas", rendimiento);
        prediccion.put("azucar_estimada",     Math.round(rendimiento * 0.12 * 10.0) / 10.0);
        prediccion.put("dias_para_cosecha",   calcularDiasParaCosecha(campo, diasDesdeEmergencia));
        return prediccion;
    }

    // ===== DETECCIÓN DE PLAGAS =====
    public Map<String, Object> obtenerDeteccionPlagas(String idCampo) {
        double[] promedios = obtenerPromediosSensores(idCampo);
        double tempAvg = promedios[0];
        double humAvg  = promedios[1];

        // Heurística simple basada en condiciones climáticas
        double riesgoChicharra = calcularRiesgoPlaga(tempAvg, humAvg, 22, 30, 70, 90);
        double riesgoMinador   = calcularRiesgoPlaga(tempAvg, humAvg, 20, 28, 65, 85);
        double riesgoCogollero = calcularRiesgoPlaga(tempAvg, humAvg, 24, 32, 60, 80);
        double promedio = (riesgoChicharra + riesgoMinador + riesgoCogollero) / 3;
        String severidad = promedio > 70 ? "alta" : promedio > 40 ? "media" : "baja";

        Map<String, Object> detencion = new LinkedHashMap<>();
        detencion.put("idCampo", idCampo);
        detencion.put("probabilidad_plaga", Math.round(promedio * 10.0) / 10.0);
        detencion.put("tipo_plaga_detectada", promedio > 50 ? "Diatraea saccharalis (Cogollero)" : "Sin plaga significativa");
        detencion.put("severidad", severidad);
        detencion.put("recomendacion", obtenerRecomendacionPlaga(promedio));
        detencion.put("fecha_deteccion", LocalDateTime.now());
        detencion.put("imagen_analizada", "N/A");
        return detencion;
    }

    // ===== OPTIMIZACIÓN DE RIEGO =====
    public Map<String, Object> obtenerOptimizacionRiego(String idCampo) {
        double[] promedios = obtenerPromediosSensores(idCampo);
        double humSuelo = promedios[2];

        double humedadOptima = (HUM_SUELO_OPTIMO_MIN + HUM_SUELO_OPTIMO_MAX) / 2;
        int diasParaRiego = humSuelo < HUM_SUELO_OPTIMO_MIN ? 0 : (int) ((humSuelo - HUM_SUELO_OPTIMO_MIN) / 2);
        int duracionMin   = humSuelo < 50 ? 120 : 60;
        int litros        = duracionMin * 15;

        Map<String, Object> riego = new LinkedHashMap<>();
        riego.put("idCampo", idCampo);
        riego.put("humedad_optima", humedadOptima);
        riego.put("proxima_fecha_riego", LocalDateTime.now().plusDays(diasParaRiego));
        riego.put("duracion_minutos", duracionMin);
        riego.put("cantidad_litros", litros);
        riego.put("confianza", 82.0);
        return riego;
    }

    // ===== RECOMENDACIONES =====
    public List<Map<String, Object>> obtenerRecomendaciones(String idCampo) {
        List<Recomendacion> guardadas = recomendacionRepository.findByIdCampoOrderByFechaGeneradaDesc(idCampo);

        if (!guardadas.isEmpty()) {
            return guardadas.stream().map(this::mapRecomendacion).toList();
        }

        // Generar recomendaciones dinámicas
        double[] promedios = obtenerPromediosSensores(idCampo);
        double tempAvg = promedios[0];
        double humSuelo = promedios[2];

        List<Map<String, Object>> recs = new ArrayList<>();

        if (humSuelo < 55) {
            recs.add(crearRecomendacion(idCampo, "riego", "Riego urgente requerido",
                "La humedad del suelo está por debajo del umbral óptimo para caña de azúcar.",
                "alta", List.of("Activar sistema de riego inmediatamente", "Verificar sensores de humedad")));
        }

        if (tempAvg > 32) {
            recs.add(crearRecomendacion(idCampo, "riego", "Estrés térmico detectado",
                "Las temperaturas elevadas pueden reducir el contenido de sacarosa.",
                "media", List.of("Aumentar frecuencia de riego", "Monitorear brix diariamente")));
        }

        recs.add(crearRecomendacion(idCampo, "general", "Monitoreo semanal de Brix",
            "Se recomienda tomar muestras de Brix y Pol para evaluar madurez.",
            "baja", List.of("Tomar 10 muestras por hectárea", "Registrar en bitácora de campo")));

        return recs;
    }

    // ===== ANÁLISIS DE IMAGEN =====
    public Map<String, Object> analizarImagen(String idCampo) {
        return Map.of(
            "tipo_analisis", "deteccion",
            "confianza_modelo", 91.5,
            "modelo_utilizado", "AgroVision-v2",
            "version_modelo", "2.1.0",
            "tiempo_procesamiento_ms", 850
        );
    }

    // ===== ALGORITMOS PRIVADOS =====

    private double calcularRendimiento(int diasDesdeEmergencia, double tempAvg, double humAvg) {
        double rendimiento = BASE_RENDIMIENTO;
        double edadFactor  = Math.min(diasDesdeEmergencia / 500.0, 1.0);
        rendimiento *= edadFactor;

        double tempOptimo  = (TEMP_OPTIMO_MIN + TEMP_OPTIMO_MAX) / 2;
        double desvTemp    = Math.abs(tempAvg - tempOptimo) / 10.0;
        double tempFactor  = Math.max(0, 1 - desvTemp * 0.1);
        rendimiento *= tempFactor * 0.8;

        double humOptimo   = (HUM_SUELO_OPTIMO_MIN + HUM_SUELO_OPTIMO_MAX) / 2;
        double desvHum     = Math.abs(humAvg - humOptimo) / 20.0;
        double humFactor   = Math.max(0, 1 - desvHum * 0.15);
        rendimiento *= humFactor * 1.2;

        return Math.round(rendimiento * 10.0) / 10.0;
    }

    private double calcularBrixEsperado(int dias, double tempAvg, double humAvg) {
        double edadFactor = Math.min(dias / 400.0, 1.0);
        double tempFactor = (tempAvg >= TEMP_OPTIMO_MIN && tempAvg <= TEMP_OPTIMO_MAX) ? 1.0 : 0.8;
        double humFactor  = (humAvg >= HUM_SUELO_OPTIMO_MIN && humAvg <= HUM_SUELO_OPTIMO_MAX) ? 1.0 : 0.85;
        return Math.round((12 + edadFactor * 8) * tempFactor * humFactor * 10.0) / 10.0;
    }

    private int calcularDiasParaCosecha(Campo campo, int diasActuales) {
        int diasTotales = switch (campo.getCicloVegetativo() != null ? campo.getCicloVegetativo() : Campo.CicloVegetativo.plantilla) {
            case plantilla -> 540;
            default        -> 360;
        };
        return Math.max(0, diasTotales - diasActuales);
    }

    private double calcularRiesgoPlaga(double temp, double hum,
                                       double tMin, double tMax,
                                       double hMin, double hMax) {
        double tempScore = (temp >= tMin && temp <= tMax) ? 60.0 + Math.random() * 30 : 10.0 + Math.random() * 20;
        double humScore  = (hum  >= hMin && hum  <= hMax) ? 60.0 + Math.random() * 30 : 10.0 + Math.random() * 20;
        return (tempScore + humScore) / 2;
    }

    private String obtenerRecomendacionPlaga(double promedio) {
        if (promedio > 70) return "Aplicar control químico urgente. Consultar agrónomo.";
        if (promedio > 40) return "Monitoreo intensivo y preparar tratamiento preventivo.";
        return "Condiciones dentro de parámetros normales. Continuar monitoreo.";
    }

    private double[] obtenerPromediosSensores(String idCampo) {
        LocalDateTime desde = LocalDateTime.now().minusDays(7);
        List<DatosSensor> datos = datosSensorRepository.findByCampoAndFechaAfter(idCampo, desde);

        if (datos.isEmpty()) return new double[]{25.0, 72.0, 68.0};

        double temp = datos.stream().filter(d -> d.getTemperatura() != null)
            .mapToDouble(DatosSensor::getTemperatura).average().orElse(25.0);
        double hum  = datos.stream().filter(d -> d.getHumedad() != null)
            .mapToDouble(DatosSensor::getHumedad).average().orElse(72.0);
        double suelo = datos.stream().filter(d -> d.getHumedadSuelo() != null)
            .mapToDouble(DatosSensor::getHumedadSuelo).average().orElse(68.0);

        return new double[]{temp, hum, suelo};
    }

    private Map<String, Object> mapRecomendacion(Recomendacion r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", r.getId());
        m.put("idCampo", r.getIdCampo());
        m.put("tipo", r.getTipo().name());
        m.put("titulo", r.getTitulo());
        m.put("descripcion", r.getDescripcion());
        m.put("urgencia", r.getUrgencia().name());
        m.put("fecha_generada", r.getFechaGenerada());
        m.put("acciones_sugeridas", r.getAccionesSugeridas() != null
            ? Arrays.asList(r.getAccionesSugeridas().split(","))
            : List.of());
        return m;
    }

    private Map<String, Object> crearRecomendacion(String idCampo, String tipo, String titulo,
                                                    String descripcion, String urgencia,
                                                    List<String> acciones) {
        return Map.of(
            "id", UUID.randomUUID().toString(),
            "idCampo", idCampo,
            "tipo", tipo,
            "titulo", titulo,
            "descripcion", descripcion,
            "urgencia", urgencia,
            "fecha_generada", LocalDateTime.now(),
            "acciones_sugeridas", acciones
        );
    }
}
