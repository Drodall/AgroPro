package com.agricola.dto.request;

import com.agricola.model.Campo;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CampoRequest {
    private String nombre;
    private Double area;
    private Campo.TipoCultivo cultivo;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaSiembra;
    private Integer estadoSalud;

    // Caña de azúcar
    private String variedad;
    private Campo.CicloVegetativo cicloVegetativo;
    private LocalDate fechaCorteAnterior;
    private Double toneladasCosechadasAnterior;
    private Double polEsperado;
}
