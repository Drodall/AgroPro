package com.agricola.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idCampo;

    @Column(nullable = false)
    private String nombre;

    private Double area; // hectáreas

    @Enumerated(EnumType.STRING)
    private TipoCultivo cultivo;

    // Ubicación
    private Double latitud;
    private Double longitud;

    private LocalDate fechaSiembra;

    private Integer estadoSalud; // 0-100

    // Campos específicos para caña de azúcar
    private String variedad; // CP72-2086, RB867515, etc.

    @Enumerated(EnumType.STRING)
    private CicloVegetativo cicloVegetativo;

    private LocalDate fechaCorteAnterior;
    private Double toneladasCosechadasAnterior;
    private Double polEsperado;

    @OneToMany(mappedBy = "campo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Sensor> sensores = new ArrayList<>();

    public enum TipoCultivo {
        maiz, trigo, arroz, papa, cana_azucar, otro
    }

    public enum CicloVegetativo {
        plantilla, resoca_1, resoca_2, resoca_3, resoca_4
    }
}
