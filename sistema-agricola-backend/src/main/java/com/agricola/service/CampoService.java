package com.agricola.service;

import com.agricola.dto.request.CampoRequest;
import com.agricola.model.Campo;
import com.agricola.repository.CampoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampoService {

    private final CampoRepository campoRepository;

    public List<Campo> obtenerTodos() {
        return campoRepository.findAll();
    }

    public Campo obtenerPorId(String id) {
        return campoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campo no encontrado: " + id));
    }

    public Campo crear(CampoRequest req) {
        Campo campo = mapearCampo(new Campo(), req);
        return campoRepository.save(campo);
    }

    public Campo actualizar(String id, CampoRequest req) {
        Campo campo = obtenerPorId(id);
        mapearCampo(campo, req);
        return campoRepository.save(campo);
    }

    public void eliminar(String id) {
        if (!campoRepository.existsById(id)) {
            throw new RuntimeException("Campo no encontrado: " + id);
        }
        campoRepository.deleteById(id);
    }

    private Campo mapearCampo(Campo campo, CampoRequest req) {
        if (req.getNombre() != null)       campo.setNombre(req.getNombre());
        if (req.getArea() != null)         campo.setArea(req.getArea());
        if (req.getCultivo() != null)      campo.setCultivo(req.getCultivo());
        if (req.getLatitud() != null)      campo.setLatitud(req.getLatitud());
        if (req.getLongitud() != null)     campo.setLongitud(req.getLongitud());
        if (req.getFechaSiembra() != null) campo.setFechaSiembra(req.getFechaSiembra());
        if (req.getEstadoSalud() != null)  campo.setEstadoSalud(req.getEstadoSalud());
        if (req.getVariedad() != null)     campo.setVariedad(req.getVariedad());
        if (req.getCicloVegetativo() != null) campo.setCicloVegetativo(req.getCicloVegetativo());
        if (req.getFechaCorteAnterior() != null) campo.setFechaCorteAnterior(req.getFechaCorteAnterior());
        if (req.getToneladasCosechadasAnterior() != null) campo.setToneladasCosechadasAnterior(req.getToneladasCosechadasAnterior());
        if (req.getPolEsperado() != null)  campo.setPolEsperado(req.getPolEsperado());
        return campo;
    }
}
