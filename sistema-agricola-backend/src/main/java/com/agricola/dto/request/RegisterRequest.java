package com.agricola.dto.request;

import com.agricola.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String contrasena;

    private Usuario.RolUsuario rol = Usuario.RolUsuario.agricultor;
}
