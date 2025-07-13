package com.example.demo.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Base;
import com.example.demo.exceptions.EntityAlreadyDisabledException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.BaseRepository;

import jakarta.transaction.Transactional;

public abstract class BaseServiceImpl <E extends Base, ID extends Serializable> implements BaseService<E, ID>{
     protected BaseRepository<E, ID> baseRepository;

    public BaseServiceImpl(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    //TODO: Borrar
    @Override
    @Transactional
    public E save(E entity) throws Exception {
        try{
            entity.setFechaHoraAlta(new Date());
            return baseRepository.save(entity);
        }
        catch(Exception e ){
            throw new Exception(e.getMessage());
        }
    }

    //TODO: Borrar
    @Override
    @Transactional
    public E update(ID id, E entity) throws Exception {
        try{
            Optional<E> entityOptional = baseRepository.findById((id));
            E entityUpdate = entityOptional.get();
            entityUpdate = baseRepository.save(entity);
            return entityUpdate;
        }
        catch(Exception e ){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean delete(ID id) {
        E entity = findById(id);
        if(entity.getFechaHoraBaja() != null){
            throw new EntityAlreadyDisabledException("La entidad ya se encuentra deshabilitada");
        }
        entity.setFechaHoraBaja(new Date());
        baseRepository.save(entity);
        return true;
    }

    //TODO: Este volaria?
    @Override
    @Transactional
    public List<E> findAll() throws Exception {
        try {
            List<E> entities = baseRepository.findAll();
            return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    @Override
    public E findById(ID id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entidad no encontrada con ID: " + id));
    }

    //TODO: Este volaria?
    @Override
    @Transactional
    public List<E> traerSoloActivos() throws Exception {
        try {
           List<E> entities = baseRepository.findByFechaHoraBajaIsNull();
           return entities;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
