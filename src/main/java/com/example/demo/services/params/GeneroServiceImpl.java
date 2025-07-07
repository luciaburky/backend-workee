package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Genero;
import com.example.demo.repositories.params.GeneroRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class GeneroServiceImpl extends BaseServiceImpl<Genero,Long> implements GeneroService {

    private GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        super(generoRepository);
        this.generoRepository = generoRepository;
    }

}
