package com.agricola.controller;

import com.agricola.dto.RespuestaAPI;
import com.agricola.service.IAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "IA y Predicciones", description = "Predicciones agronómicas con inteligencia artificial")
public class IAController {

    private final IAService iaService;

    @GetMapping("/predictions/{idCampo}")
    @Operation(summary = "Obtener todas las predicciones del campo (cosecha, plagas, riego, recomendaciones)")
    public ResponseEntity<RespuestaAPI<?>> obtenerPredicciones(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(iaService.obtenerPredicciones(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/crop-prediction/{idCampo}")
    @Operation(summary = "Predicción de rendimiento de cosecha")
    public ResponseEntity<RespuestaAPI<?>> predecirCosecha(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(iaService.predecirCosecha(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/plague-detection/{idCampo}")
    @Operation(summary = "Detección de plagas por condiciones climáticas")
    public ResponseEntity<RespuestaAPI<?>> detectarPlagas(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(iaService.obtenerDeteccionPlagas(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @PostMapping("/plague-detection")
    @Operation(summary = "Analizar imagen para detección de plagas")
    public ResponseEntity<RespuestaAPI<?>> analizarImagenPlagas(
            @RequestParam("fieldId") String fieldId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(RespuestaAPI.ok(iaService.obtenerDeteccionPlagas(fieldId),
            "Análisis de imagen completado"));
    }

    @GetMapping("/irrigation-optimization/{idCampo}")
    @Operation(summary = "Optimización de riego")
    public ResponseEntity<RespuestaAPI<?>> optimizarRiego(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(iaService.obtenerOptimizacionRiego(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/recommendations/{idCampo}")
    @Operation(summary = "Obtener recomendaciones agronómicas personalizadas")
    public ResponseEntity<RespuestaAPI<?>> obtenerRecomendaciones(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(iaService.obtenerRecomendaciones(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @PostMapping("/analyze-plant")
    @Operation(summary = "Análisis de imagen de planta")
    public ResponseEntity<RespuestaAPI<?>> analizarPlanta(
            @RequestParam("fieldId") String fieldId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(RespuestaAPI.ok(iaService.analizarImagen(fieldId)));
    }
}
