package com.agricola.dto.response;

import com.agricola.model.Usuario;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UsuarioResponse {
    private String idUsuario;
    private String nombre;
    private String email;
    private String rol;
    private List<String> camposAsignados;
    private LocalDateTime fechaRegistro;

    public static UsuarioResponse from(Usuario u) {
        List<String> campos;
        try {
            campos = u.getCamposAsignados() != null ? new java.util.ArrayList<>(u.getCamposAsignados()) : new java.util.ArrayList<>();
        } catch (Exception e) {
            campos = new java.util.ArrayList<>();
        }
        return UsuarioResponse.builder()
            .idUsuario(u.getIdUsuario())
            .nombre(u.getNombre())
            .email(u.getEmail())
            .rol(u.getRol().name())
            .camposAsignados(campos)
            .fechaRegistro(u.getFechaRegistro())
            .build();
    }
}
