package com.agricola.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recomendaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String idCampo;

    @Enumerated(EnumType.STRING)
    private TipoRecomendacion tipo;

    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private NivelUrgencia urgencia;

    private LocalDateTime fechaGenerada;

    @Column(length = 2000)
    private String accionesSugeridas; // CSV simple

    @PrePersist
    protected void onCreate() {
        if (fechaGenerada == null) fechaGenerada = LocalDateTime.now();
    }

    public enum TipoRecomendacion {
        riego, fertilizante, plagas, cosecha, general
    }

    public enum NivelUrgencia {
        baja, media, alta, critica
    }
}
