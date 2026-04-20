package com.agricola.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaAPI<T> {
    private boolean exito;
    private T datos;
    private String mensaje;
    private Integer codigoError;

    public static <T> RespuestaAPI<T> ok(T datos) {
        return RespuestaAPI.<T>builder()
            .exito(true)
            .datos(datos)
            .mensaje("OK")
            .build();
    }

    public static <T> RespuestaAPI<T> ok(T datos, String mensaje) {
        return RespuestaAPI.<T>builder()
            .exito(true)
            .datos(datos)
            .mensaje(mensaje)
            .build();
    }

    public static <T> RespuestaAPI<T> error(String mensaje, int codigo) {
        return RespuestaAPI.<T>builder()
            .exito(false)
            .mensaje(mensaje)
            .codigoError(codigo)
            .build();
    }
}
