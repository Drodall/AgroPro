package com.agricola.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "metricas_cana")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricasCana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idCampo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private Double temperatura;
    private Double brix;          // Sólidos solubles (12-22%)
    private Double pol;           // Sacarosa (10-18%)
    private Double pureza;        // % (75-90%)
    private Double fibra;         // % (10-15%)
    private Double humedadHoja;   // % (65-75%)
    private Double phSuelo;       // (5.5-7.5)
    private Double densidadTallos;// tallos/m²
    private Double alturaPromedio;// cm
    private Double humedadSuelo;  // %

    @PrePersist
    protected void onCreate() {
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
