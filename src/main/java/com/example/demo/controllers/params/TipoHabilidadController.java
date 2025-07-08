package com.example.demo.controllers.params;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.BaseControllerImpl;
import com.example.demo.entities.params.TipoHabilidad;
import com.example.demo.services.params.TipoHabilidadService;

@RestController
@RequestMapping("/tipoHabilidades")
public class TipoHabilidadController extends BaseControllerImpl<TipoHabilidad, TipoHabilidadService> {

    private final TipoHabilidadService tipoHabilidadService;

    public TipoHabilidadController(TipoHabilidadService tipoHabilidadService) {
        super(tipoHabilidadService);
        this.tipoHabilidadService = tipoHabilidadService;
    }    
}
