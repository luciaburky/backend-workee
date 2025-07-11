package com.example.demo.services.params;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.params.PaisRequestDTO;
import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;
import com.example.exceptions.EntityAlreadyExistsException;

import jakarta.transaction.Transactional;


@Service
public class PaisServiceImpl extends BaseServiceImpl<Pais,Long> implements PaisService{
    private final PaisRepository paisRepository;


    public PaisServiceImpl(PaisRepository paisRepository) {
        super(paisRepository);
        this.paisRepository = paisRepository;
    }

    @Override
    @Transactional
    public Pais guardarPais(PaisRequestDTO paisRequestDTO) {
        Optional<Pais> paisExistente = paisRepository.findByNombrePaisIgnoreCase(paisRequestDTO.getNombrePais());
        if (paisExistente.isPresent()) {
            throw new EntityAlreadyExistsException("Ya existe un país con ese nombre");
        }

        Pais nuevoPais = new Pais();
        nuevoPais.setNombrePais(paisRequestDTO.getNombrePais());
        nuevoPais.setFechaHoraAlta(new Date());

        return paisRepository.save(nuevoPais);
    }

    
    
}
