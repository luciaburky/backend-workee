package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Pais;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class PaisServiceImpl extends BaseServiceImpl<Pais,Long> implements PaisService{
    private final PaisRepository paisRepository;

    public PaisServiceImpl(PaisRepository paisRepository) {
        super(paisRepository);
        this.paisRepository = paisRepository;
    }
}
