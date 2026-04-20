package com.agricola.controller;

import com.agricola.dto.RespuestaAPI;
import com.agricola.dto.request.LoginRequest;
import com.agricola.dto.request.RegisterRequest;
import com.agricola.model.Usuario;
import com.agricola.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login, registro y perfil de usuario")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con email y contraseña")
    public ResponseEntity<RespuestaAPI<?>> login(@Valid @RequestBody LoginRequest request) {
        try {
            var tokenResponse = authService.login(request);
            return ResponseEntity.ok(RespuestaAPI.ok(tokenResponse, "Login exitoso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                .body(RespuestaAPI.error(e.getMessage(), 401));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión (token invalidation en cliente)")
    public ResponseEntity<RespuestaAPI<?>> logout() {
        return ResponseEntity.ok(RespuestaAPI.ok(null, "Sesión cerrada exitosamente"));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<RespuestaAPI<?>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            var usuario = authService.registrar(request);
            return ResponseEntity.status(201)
                .body(RespuestaAPI.ok(usuario, "Usuario registrado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                .body(RespuestaAPI.error(e.getMessage(), 400));
        }
    }

    @GetMapping("/profile")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<RespuestaAPI<?>> perfil(Authentication auth) {
        try {
            Usuario usuario = (Usuario) auth.getPrincipal();
            var perfil = authService.obtenerPerfilActual(usuario.getEmail());
            return ResponseEntity.ok(RespuestaAPI.ok(perfil));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                .body(RespuestaAPI.error(e.getMessage(), 404));
        }
    }
}
