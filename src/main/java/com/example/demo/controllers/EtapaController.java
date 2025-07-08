package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.Etapa;
import com.example.demo.services.EtapaService;

@RestController
@RequestMapping("/etapas")
public class EtapaController extends BaseControllerImpl<Etapa, EtapaService> {

    private final EtapaService etapaService;

    public EtapaController(EtapaService etapaService) {
        super(etapaService);
        this.etapaService = etapaService;
    }
    
}
