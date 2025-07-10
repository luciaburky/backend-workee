package com.example.demo.services.params;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Etapa;
import com.example.demo.repositories.params.EtapaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class EtapaServiceImpl extends BaseServiceImpl<Etapa, Long> implements EtapaService {

    private final EtapaRepository etapaRepository;

    public EtapaServiceImpl(EtapaRepository etapaRepository) {
        super(etapaRepository);
        this.etapaRepository = etapaRepository;
    }
}
