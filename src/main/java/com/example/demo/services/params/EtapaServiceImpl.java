package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.EtapaRequestDTO;
import com.example.demo.entities.params.Etapa;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.params.EtapaRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class EtapaServiceImpl extends BaseServiceImpl<Etapa, Long> implements EtapaService {

    private final EtapaRepository etapaRepository;

    public EtapaServiceImpl(EtapaRepository etapaRepository) {
        super(etapaRepository);
        this.etapaRepository = etapaRepository;
    }

    @Override
    @Transactional
    public Etapa guardarEtapa(EtapaRequestDTO etapaDTO) {
        if (yaExisteEtapa(etapaDTO.getNombreEtapa())) {
            throw new EntityAlreadyExistsException("Ya existe una etapa con ese nombre");
        }

        Etapa nuevaEtapa = new Etapa();
        nuevaEtapa.setNombreEtapa(etapaDTO.getNombreEtapa());
        nuevaEtapa.setDescripcionEtapa(etapaDTO.getDescripcionEtapa());
        nuevaEtapa.setFechaHoraAlta(new Date());

        return etapaRepository.save(nuevaEtapa);
    }

    @Override
    @Transactional
    public Etapa actualizarEtapa(Long id, EtapaRequestDTO etapaDTO) {
        Etapa etapaOriginal = this.findById(id);

        if (yaExisteEtapa(etapaDTO.getNombreEtapa())) {
            throw new EntityAlreadyExistsException("Ya existe una etapa con ese nombre");
        }

        etapaOriginal.setNombreEtapa(etapaDTO.getNombreEtapa());
        etapaOriginal.setDescripcionEtapa(etapaDTO.getDescripcionEtapa());
        return etapaRepository.save(etapaOriginal);
    }

    @Override
    @Transactional
    public Boolean habilitarEtapa(Long id) {
        if (id == null) {
            throw new EntityNotValidException("El ID de la etapa no puede ser nulo");
        }

        Etapa etapa = this.findById(id);
        if (etapa.getFechaHoraBaja() == null) {
            throw new EntityNotValidException("La etapa ya está habilitada");
        }

        etapa.setFechaHoraBaja(null);
        etapaRepository.save(etapa);
        return true;
    }
    
    @Override
    public List<Etapa> obtenerEtapas() {
        return etapaRepository.findAllByOrderByNombreEtapaAsc();
    }

    @Override
    public List<Etapa> obtenerEtapasActivos() {
        return etapaRepository.buscarEtapasActivos();
    }

    private Boolean yaExisteEtapa(String nombreEtapa) {
        Optional<Etapa> etapaExiste = etapaRepository.findByNombreEtapaIgnoreCase(nombreEtapa);
        return etapaExiste.isPresent();
    }
}
