package com.example.demo.services.oferta;

import com.example.demo.dtos.OfertaRequestDTO;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.services.BaseService;

public interface OfertaService extends BaseService<Oferta, Long>{
    Oferta crearOferta(OfertaRequestDTO ofertaDTO);    
}
