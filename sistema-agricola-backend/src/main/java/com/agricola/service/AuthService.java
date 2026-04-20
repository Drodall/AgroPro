package com.agricola.service;

import com.agricola.dto.request.LoginRequest;
import com.agricola.dto.request.RegisterRequest;
import com.agricola.dto.response.TokenResponse;
import com.agricola.dto.response.UsuarioResponse;
import com.agricola.model.Usuario;
import com.agricola.repository.UsuarioRepository;
import com.agricola.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(usuario.getEmail());

        return TokenResponse.builder()
            .token(token)
            .usuario(UsuarioResponse.from(usuario))
            .build();
    }

    public UsuarioResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
            .nombre(request.getNombre())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getContrasena()))
            .rol(request.getRol() != null ? request.getRol() : Usuario.RolUsuario.agricultor)
            .camposAsignados(new ArrayList<>())
            .build();

        usuarioRepository.save(usuario);
        return UsuarioResponse.from(usuario);
    }

    public UsuarioResponse obtenerPerfilActual(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioResponse.from(usuario);
    }
}
