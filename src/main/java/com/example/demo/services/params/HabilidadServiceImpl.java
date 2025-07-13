package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.HabilidadRequestDTO;
import com.example.demo.entities.params.Habilidad;
import com.example.demo.entities.params.TipoHabilidad;
import com.example.demo.exceptions.EntityAlreadyEnabledException;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.params.HabilidadRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class HabilidadServiceImpl extends BaseServiceImpl<Habilidad, Long> implements HabilidadService {
    
    private final HabilidadRepository habilidadRepository;
    private final TipoHabilidadService tipoHabilidadService;
    
    public HabilidadServiceImpl(HabilidadRepository habilidadRepository, TipoHabilidadService tipoHabilidadService){
        super(habilidadRepository);
        this.habilidadRepository = habilidadRepository;
        this.tipoHabilidadService = tipoHabilidadService;
    }

    @Override
    public List<Habilidad> findHabilidadByTipoHabilidad(Long idTipoHabilidad) {
        if (idTipoHabilidad == null) {
            throw new EntityNotValidException("El ID del tipo de habilidad no puede ser nulo");
        }

        tipoHabilidadService.findById(idTipoHabilidad);

        return habilidadRepository.findByTipoHabilidad(idTipoHabilidad);
    }


    @Override
    @Transactional

    public Habilidad guardarHabilidad(HabilidadRequestDTO habilidadDTO){
        if(yaExisteHabilidad(habilidadDTO.getNombreHabilidad())){
            throw new EntityAlreadyExistsException("Ya existe una habilidad con ese nombre");
        }

        Habilidad nuevaHabilidad = new Habilidad();
        nuevaHabilidad.setNombreHabilidad(habilidadDTO.getNombreHabilidad());
        nuevaHabilidad.setFechaHoraAlta(new Date());

        if(habilidadDTO.getIdTipoHabilidad() == null) {
            throw new EntityNotValidException(  "El ID del tipo de habilidad no puede ser nulo");
        }
        
        TipoHabilidad tipoHabilidadBuscada = tipoHabilidadService.findById(habilidadDTO.getIdTipoHabilidad());

        nuevaHabilidad.setTipoHabilidad(tipoHabilidadBuscada);

        return habilidadRepository.save(nuevaHabilidad);
    }

    @Override
    @Transactional
    public Habilidad actualizarHabilidad(Long id, HabilidadRequestDTO habilidadDTO){
        if(habilidadDTO.getNombreHabilidad() == null || habilidadDTO.getNombreHabilidad().isEmpty()) {
            throw new EntityNotValidException("El nombre de la habilidad no puede ser nulo o vacío");
        }

        Habilidad habilidadOriginal = findById(id);

        if(yaExisteHabilidad(habilidadDTO.getNombreHabilidad())){
            throw new EntityAlreadyExistsException("Ya existe una habilidad con ese nombre");
        }

        habilidadOriginal.setNombreHabilidad(habilidadDTO.getNombreHabilidad());

        return habilidadRepository.save(habilidadOriginal);
    }

    @Override
    @Transactional
    public Boolean habilitarHabilidad(Long id){
        if(id == null) {
            throw new EntityNotValidException("El ID de la habilidad no puede ser nulo");
        }
        Habilidad habilidadOriginal = findById(id);
        if(habilidadOriginal.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("La habilidad ya está habilitada");
        }
        habilidadOriginal.setFechaHoraBaja(null);
        habilidadRepository.save(habilidadOriginal);
        return true;
    }

    
    @Override
    public List<Habilidad> obtenerHabilidades() {
        return habilidadRepository.findAllByOrderByNombreHabilidadAsc();
    }

    @Override
    public List<Habilidad> obtenerHabilidadesActivas() {
        return habilidadRepository.buscarHabilidadesActivas();
    }

    private Boolean yaExisteHabilidad(String nombreHabilidad) {
        Optional<Habilidad> habilidadExiste = habilidadRepository.findByNombreHabilidadIgnoreCase(nombreHabilidad);
        return habilidadExiste.isPresent();
    }
}