package com.agricola.controller;

import com.agricola.dto.RespuestaAPI;
import com.agricola.dto.request.CampoRequest;
import com.agricola.service.CampoService;
import com.agricola.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
@Tag(name = "Campos", description = "CRUD de campos agrícolas")
public class CampoController {

    private final CampoService campoService;
    private final SensorService sensorService;

    @GetMapping
    @Operation(summary = "Obtener todos los campos")
    public ResponseEntity<RespuestaAPI<?>> obtenerTodos() {
        return ResponseEntity.ok(RespuestaAPI.ok(campoService.obtenerTodos()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener campo por ID")
    public ResponseEntity<RespuestaAPI<?>> obtenerPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(campoService.obtenerPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo campo")
    public ResponseEntity<RespuestaAPI<?>> crear(@RequestBody CampoRequest request) {
        return ResponseEntity.status(201)
            .body(RespuestaAPI.ok(campoService.crear(request), "Campo creado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar campo existente")
    public ResponseEntity<RespuestaAPI<?>> actualizar(@PathVariable String id,
                                                       @RequestBody CampoRequest request) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(campoService.actualizar(id, request), "Campo actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar campo")
    public ResponseEntity<RespuestaAPI<?>> eliminar(@PathVariable String id) {
        try {
            campoService.eliminar(id);
            return ResponseEntity.ok(RespuestaAPI.ok(java.util.Map.of("success", true)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }

    @GetMapping("/{idCampo}/sensors")
    @Operation(summary = "Obtener todos los sensores de un campo")
    public ResponseEntity<RespuestaAPI<?>> obtenerSensoresDeCampo(@PathVariable String idCampo) {
        try {
            return ResponseEntity.ok(RespuestaAPI.ok(sensorService.obtenerSensoresDeCampo(idCampo)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }
}
