package com.agricola.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "ciclos_historial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idCampo;

    private String ciclo; // plantilla, resoca_1, etc.
    private LocalDate fechaCorte;
    private Double toneladasCosechadas;
    private Double brixPromedio;
    private Double polPromedio;

    @Column(length = 500)
    private String incidenciaPlagas; // JSON simple: "carbunclo,roya"
}
