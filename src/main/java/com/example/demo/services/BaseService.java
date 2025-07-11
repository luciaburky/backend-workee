package com.example.demo.services;

import java.io.Serializable;
import java.util.List;

import com.example.demo.entities.Base;

public interface BaseService <E extends Base, ID extends Serializable>{
    public E findById(ID id) /*throws Exception*/;
    public List<E> findAll() throws Exception;
    public E save(E entity) throws Exception;
    public E update (ID id, E entity) throws Exception;
    public boolean delete(ID id) /*throws Exception*/;

    public List<E> traerSoloActivos() throws Exception;

}