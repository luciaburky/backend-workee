package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.params.Rubro;
import com.example.demo.services.RubroService;

@RestController
@RequestMapping("/rubros")
public class RubroController extends BaseControllerImpl<Rubro, RubroService> {

    private final RubroService rubroService;

    public RubroController(RubroService rubroService) {
        super(rubroService);
        this.rubroService = rubroService;
    }
    
}
