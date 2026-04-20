package com.agricola.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sensorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campo_id", nullable = false)
    private Campo campo;

    @Enumerated(EnumType.STRING)
    private EstadoSensor estado;

    private Double latitud;
    private Double longitud;

    private String nombre;
    private String tipo; // temperatura, humedad, suelo, etc.

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DatosSensor> historialDatos = new ArrayList<>();

    public enum EstadoSensor {
        activo, inactivo, error
    }
}
