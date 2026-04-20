package com.agricola.controller;

import com.agricola.dto.RespuestaAPI;
import com.agricola.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
@Tag(name = "Sensores", description = "Gestión de sensores IoT y datos en tiempo real")
public class SensorController {

    private final SensorService sensorService;

    @GetMapping("/{sensorId}")
    @Operation(summary = "Obtener último dato de un sensor específico")
    public ResponseEntity<RespuestaAPI<?>> obtenerSensor(@PathVariable String sensorId) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(sensorService.obtenerUltimoDato(sensorId)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @PostMapping("/data")
    @Operation(summary = "Enviar datos desde un sensor")
    public ResponseEntity<RespuestaAPI<?>> enviarDatos(@RequestBody Map<String, Object> datos) {
        try {
            sensorService.guardarDatos(datos);
            return ResponseEntity.ok(RespuestaAPI.ok(Map.of("success", true)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(RespuestaAPI.error(e.getMessage(), 400));
        }
    }

    @GetMapping("/history/{idCampo}")
    @Operation(summary = "Obtener historial de sensores de un campo")
    public ResponseEntity<RespuestaAPI<?>> obtenerHistorial(
            @PathVariable String idCampo,
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(RespuestaAPI.ok(sensorService.obtenerHistorial(idCampo, dias)));
    }
}
