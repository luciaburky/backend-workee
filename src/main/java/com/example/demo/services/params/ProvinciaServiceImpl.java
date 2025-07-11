package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.ProvinciaRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.ProvinciaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.exceptions.EntityAlreadyEnabledException;
import com.example.exceptions.EntityAlreadyExistsException;
import com.example.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class ProvinciaServiceImpl extends BaseServiceImpl<Provincia,Long> implements ProvinciaService{

    private final ProvinciaRepository provinciaRepository;
    private final PaisService paisService;


    public ProvinciaServiceImpl(ProvinciaRepository provinciaRepository, PaisService paisService) {
        super(provinciaRepository);
        this.provinciaRepository = provinciaRepository;
        this.paisService = paisService;
    }

    @Override
    public List<Provincia> findProvinciaByPaisId(Long idPais) {
        try{
            return provinciaRepository.findProvinciaByPaisId(idPais);
        } catch (Exception e){
            throw new RuntimeException("Error al acceder a los datos de provincias.", e);
        }
    }

    @Override
    @Transactional
    public Provincia guardarProvincia(ProvinciaRequestDTO provinciaRequestDTO){
        if(yaExisteProvincia(provinciaRequestDTO.getNombreProvincia())){
            throw new EntityAlreadyExistsException("Ya existe una provincia con ese nombre");
        }

        Provincia nuevaProvincia = new Provincia();
        nuevaProvincia.setNombreProvincia(provinciaRequestDTO.getNombreProvincia());
        nuevaProvincia.setFechaHoraAlta(new Date());

        if(provinciaRequestDTO.getIdPais() == null) {
            throw new IllegalArgumentException("El ID del país no puede ser nulo");
        }
        Pais paisBuscado = paisService.buscarPaisPorId(provinciaRequestDTO.getIdPais());
        nuevaProvincia.setPais(paisBuscado);

        return provinciaRepository.save(nuevaProvincia);
    }

    @Override
    @Transactional
    public Provincia actualizarProvincia(Long id, ProvinciaRequestDTO provinciaRequestDTO){
        if(provinciaRequestDTO.getNombreProvincia() == null || provinciaRequestDTO.getNombreProvincia().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la provincia no puede ser nulo o vacío");
        }

        Provincia provinciaOriginal = buscarProvinciaPorId(id);

        if(yaExisteProvincia(provinciaRequestDTO.getNombreProvincia())){
            throw new EntityAlreadyExistsException("Ya existe una provincia con ese nombre");
        }

        provinciaOriginal.setNombreProvincia(provinciaRequestDTO.getNombreProvincia());

        return provinciaRepository.save(provinciaOriginal);
    }

    @Override
    public Provincia buscarProvinciaPorId(Long id){
        return provinciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia no encontrada con ID: " + id));
    }

    @Override
    public List<Provincia> obtenerProvincias(){
        return provinciaRepository.findAllByOrderByNombreProvinciaAsc();
    }

    @Override
    public List<Provincia> obtenerProvinciasActivas(){
        return provinciaRepository.buscarProvinciasActivas();
    }

    @Override
    @Transactional
    public Boolean habilitarProvincia(Long id){
        if(id == null) {
            throw new IllegalArgumentException("El ID de la provincia no puede ser nulo");
        }
        Provincia provinciaOriginal = buscarProvinciaPorId(id);
        if(provinciaOriginal.getFechaHoraBaja() == null) {
            throw new EntityAlreadyEnabledException("La provincia ya está habilitada");
        }
        provinciaOriginal.setFechaHoraBaja(null);
        provinciaRepository.save(provinciaOriginal);
        return true;
    }

    @Override
    @Transactional
    public Boolean deshabilitarProvincia(Long id){
        if(id == null) {
            throw new IllegalArgumentException("El ID de la provincia no puede ser nulo");
        }
        Provincia provinciaOriginal = buscarProvinciaPorId(id);
        if(provinciaOriginal.getFechaHoraBaja() != null) {
            throw new EntityAlreadyEnabledException("La provincia ya está deshabilitada");
        }
        provinciaOriginal.setFechaHoraBaja(new Date());
        provinciaRepository.save(provinciaOriginal);
        return true;
    }


    private Boolean yaExisteProvincia(String nombreProvincia) {
        Optional<Provincia> provinciaExistente = provinciaRepository.findByNombreProvinciaIgnoreCase(nombreProvincia);
        return provinciaExistente.isPresent();
    }
}
