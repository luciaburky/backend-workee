package com.example.demo.services.params;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.TipoEventoRequestDTO;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.repositories.params.TipoEventoRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.exceptions.EntityAlreadyDisabledException;
import com.example.exceptions.EntityAlreadyEnabledException;
import com.example.exceptions.EntityAlreadyExistsException;
import com.example.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class TipoEventoServiceImpl extends BaseServiceImpl<TipoEvento,Long> implements TipoEventoService {
    private final TipoEventoRepository tipoEventoRepository;

    public TipoEventoServiceImpl(TipoEventoRepository tipoEventoRepository) {
        super(tipoEventoRepository);
        this.tipoEventoRepository = tipoEventoRepository;
    }

    @Override
    @Transactional
    public TipoEvento guardarTipoEvento(TipoEventoRequestDTO tipoEventoRequestDTO) {
        if(yaExisteTipoEvento(tipoEventoRequestDTO.getNombreTipoEvento())) {
            throw new EntityAlreadyExistsException("Ya existe un tipo de evento con ese nombre");
        }
        TipoEvento nuevTipoEvento = new TipoEvento();
        nuevTipoEvento.setNombreTipoEvento(tipoEventoRequestDTO.getNombreTipoEvento());
        nuevTipoEvento.setFechaHoraAlta(new Date());
        return tipoEventoRepository.save(nuevTipoEvento);
    }

    @Override
    @Transactional
    public TipoEvento actualizarTipoEvento(Long id, TipoEventoRequestDTO tipoEventoRequestDTO) {
        if(yaExisteTipoEvento(tipoEventoRequestDTO.getNombreTipoEvento())) {
            throw new EntityAlreadyExistsException("Ya existe un tipo de evento con ese nombre");
        }
        TipoEvento tipoEventoOriginal = buscarTipoEventoPorId(id);
        tipoEventoOriginal.setNombreTipoEvento(tipoEventoRequestDTO.getNombreTipoEvento());
        return tipoEventoRepository.save(tipoEventoOriginal);
    }

    @Override
    public TipoEvento buscarTipoEventoPorId(Long id) {
        return tipoEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de evento no encontrado con ID: " + id));
    }

    @Override
    public List<TipoEvento> obtenerTiposEventos() {
        return tipoEventoRepository.findAllByOrderByNombreTipoEventoOfertaAsc();
    }

    @Override
    public List<TipoEvento> obtenerTiposEventosActivos() {
        return tipoEventoRepository.buscarTiposEventosActivos();
    }

    @Override
    @Transactional
    public Boolean habilitarTipoEvento(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        TipoEvento tipoEventoOriginal = buscarTipoEventoPorId(id);
        if(tipoEventoOriginal.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("El tipo de evento ya está habilitado");
        }
        tipoEventoOriginal.setFechaHoraBaja(null);
        tipoEventoRepository.save(tipoEventoOriginal);
        return true;
    }

    @Override
    @Transactional
    public Boolean deshabilitarTipoEvento(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        TipoEvento tipoEventoOriginal = buscarTipoEventoPorId(id);
        if(tipoEventoOriginal.getFechaHoraBaja() != null) {
            throw new EntityAlreadyDisabledException("El tipo de evento ya está deshabilitado");
        }
        tipoEventoOriginal.setFechaHoraBaja(new Date());
        tipoEventoRepository.save(tipoEventoOriginal);
        return true;
    }

    private Boolean yaExisteTipoEvento(String nombreTipoEvento) {
        return tipoEventoRepository.findByNombreTipoEventoIgnoreCase(nombreTipoEvento).isPresent();
    }
}
