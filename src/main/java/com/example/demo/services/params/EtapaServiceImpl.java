package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.EtapaRequestDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.params.Etapa;
import com.example.demo.exceptions.EntityAlreadyExistsException;
import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.params.EtapaRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.demo.services.empresa.EmpresaService;

import jakarta.transaction.Transactional;

@Service
public class EtapaServiceImpl extends BaseServiceImpl<Etapa, Long> implements EtapaService {

    private final EtapaRepository etapaRepository;
    private final EmpresaService empresaService;

    public EtapaServiceImpl(EtapaRepository etapaRepository, EmpresaService empresaService) {
        super(etapaRepository);
        this.etapaRepository = etapaRepository;
        this.empresaService = empresaService;
    }

    @Override
    @Transactional
    public Etapa crearPredeterminada(EtapaRequestDTO etapaDTO) {
        if (yaExisteEtapa(etapaDTO.getNombreEtapa(), null)) {
            throw new EntityAlreadyExistsException("Ya existe una etapa con ese nombre");
        }

        Etapa nuevaEtapa = new Etapa();
        nuevaEtapa.setNombreEtapa(etapaDTO.getNombreEtapa());
        nuevaEtapa.setDescripcionEtapa(etapaDTO.getDescripcionEtapa());
        nuevaEtapa.setEsPredeterminada(true);
        nuevaEtapa.setEmpresa(null);
        nuevaEtapa.setFechaHoraAlta(new Date());

        return etapaRepository.save(nuevaEtapa);
    }

    @Override
    @Transactional
    public Etapa crearPropia(Long empresaId, EtapaRequestDTO etapaDTO) {
        //cambiar para que verifique solo en las propias 
        boolean existeNombre = etapaRepository.existsByNombreEtapaIgnoreCaseAndEmpresaIdAndEsPredeterminadaFalse(etapaDTO.getNombreEtapa().trim(), empresaId);
        if (existeNombre) {
            throw new EntityAlreadyExistsException("Ya existe una etapa con ese nombre");
        }

        Empresa empresa = empresaService.findById(empresaId);
        Etapa nuevaEtapa = new Etapa();
        nuevaEtapa.setNombreEtapa(etapaDTO.getNombreEtapa());
        nuevaEtapa.setDescripcionEtapa(etapaDTO.getDescripcionEtapa());
        nuevaEtapa.setEsPredeterminada(false);
        nuevaEtapa.setEmpresa(empresa);
        nuevaEtapa.setFechaHoraAlta(new Date());

        return etapaRepository.save(nuevaEtapa);
    }

    @Override
    @Transactional
    public Etapa actualizarEtapa(Long id, EtapaRequestDTO etapaDTO) {
        Etapa etapaOriginal = this.findById(id);

        // Si viene un nombre no vacío, se valida y se setea
        if (etapaDTO.getNombreEtapa() != null && !etapaDTO.getNombreEtapa().isBlank()) {
            // Si el nombre es distinto al actual, valido duplicado
            if (!etapaDTO.getNombreEtapa().equalsIgnoreCase(etapaOriginal.getNombreEtapa()) &&
                    yaExisteEtapa(etapaDTO.getNombreEtapa(), id)) {
                throw new EntityAlreadyExistsException("Ya existe una etapa con ese nombre");
            }
            etapaOriginal.setNombreEtapa(etapaDTO.getNombreEtapa());
        }

        // Si viene una descripción no vacía, se setea
        if (etapaDTO.getDescripcionEtapa() != null && !etapaDTO.getDescripcionEtapa().isBlank()) {
            etapaOriginal.setDescripcionEtapa(etapaDTO.getDescripcionEtapa());
        }

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

    private Boolean yaExisteEtapa(String nombreEtapa, Long idAExcluir) {
        Optional<Etapa> etapaExiste = etapaRepository.findByNombreEtapaIgnoreCase(nombreEtapa);
        return etapaExiste
        .filter(e -> idAExcluir == null || !e.getId().equals(idAExcluir))
        .isPresent();
    }

    //TODO
    @Override
    @Transactional
    public void deshabilitarEtapa(Long idEtapa){
        return;
    }

    /* TODO
    @Override
    @Transactional
    public void eliminarEtapaPropia(Long idEtapa){
        Optional<Etapa> etapa = etapaRepository.findById(idEtapa);

        if(etapa.getEsPredeterminada().equals(true)){
            throw new IllegalStateException("No se puede eliminar una etapa predeterminada");
        }

        boolean enUso = ofertaEtapa
    }
        */

        //TODO

}
