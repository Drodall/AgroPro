package com.agricola.controller;

import com.agricola.dto.RespuestaAPI;
import com.agricola.service.CaniaAzucarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cana")
@RequiredArgsConstructor
@Tag(name = "Caña de Azúcar", description = "Módulo especializado para cultivo de caña de azúcar")
public class CaniaAzucarController {

    private final CaniaAzucarService caniaAzucarService;

    @GetMapping("/metricas/{idCampo}")
    @Operation(summary = "Obtener métricas actuales (Brix, Pol, Pureza, Fibra, pH, etc.)")
    public ResponseEntity<RespuestaAPI<?>> obtenerMetricas(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerMetricas(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/prediccion/{idCampo}")
    @Operation(summary = "Predicción de cosecha específica para caña")
    public ResponseEntity<RespuestaAPI<?>> obtenerPrediccion(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerPrediccion(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/plagas/{idCampo}")
    @Operation(summary = "Análisis de plagas específicas de caña de azúcar")
    public ResponseEntity<RespuestaAPI<?>> obtenerPlagas(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerPlagas(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @PostMapping("/plagas-detection")
    @Operation(summary = "Detectar plagas de caña mediante imagen")
    public ResponseEntity<RespuestaAPI<?>> detectarPlagasImagen(
            @RequestParam("fieldId") String fieldId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerPlagas(fieldId),
            "Análisis de imagen completado"));
    }

    @GetMapping("/analisis-suelo/{idCampo}")
    @Operation(summary = "Análisis completo del suelo (N, P, K, micronutrientes)")
    public ResponseEntity<RespuestaAPI<?>> obtenerAnalisisSuelo(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerAnalisisSuelo(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/recomendacion-fertilizacion/{idCampo}")
    @Operation(summary = "Recomendación de fertilización según ciclo vegetativo")
    public ResponseEntity<RespuestaAPI<?>> obtenerRecomendacionFertilizacion(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerRecomendacionFertilizacion(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/estimacion-cosecha/{idCampo}")
    @Operation(summary = "Estimación detallada de cosecha con fecha óptima")
    public ResponseEntity<RespuestaAPI<?>> estimarCosecha(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.estimarCosecha(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/calidad-jugo/{idCampo}")
    @Operation(summary = "Análisis de calidad del jugo (Brix, Pol, Pureza, rendimiento azúcar)")
    public ResponseEntity<RespuestaAPI<?>> obtenerCalidadJugo(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerCalidadJugo(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/recomendacion-riego/{idCampo}")
    @Operation(summary = "Recomendación de riego adaptada a etapa del cultivo")
    public ResponseEntity<RespuestaAPI<?>> obtenerRecomendacionRiego(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerRecomendacionRiego(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/historial-ciclos/{idCampo}")
    @Operation(summary = "Historial de ciclos anteriores (plantilla, resocas)")
    public ResponseEntity<RespuestaAPI<?>> obtenerHistorialCiclos(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.obtenerHistorialCiclos(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/impacto-climatico/{idCampo}")
    @Operation(summary = "Pronóstico de impacto climático en rendimiento")
    public ResponseEntity<RespuestaAPI<?>> analizarImpactoClimatico(
            @PathVariable String idCampo,
            @RequestParam(defaultValue = "7") int dias) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(caniaAzucarService.analizarImpactoClimatico(idCampo, dias)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }
}
