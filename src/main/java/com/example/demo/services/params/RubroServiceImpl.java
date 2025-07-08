package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Rubro;
import com.example.demo.repositories.params.RubroRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class RubroServiceImpl extends BaseServiceImpl<Rubro, Long> implements RubroService {

    private final RubroRepository rubroRepository;

    public RubroServiceImpl(RubroRepository rubroRepository) {
        super(rubroRepository);
        this.rubroRepository = rubroRepository;
    }   
    
}
