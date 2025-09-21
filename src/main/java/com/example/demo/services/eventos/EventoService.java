package com.example.demo.services.eventos;

import org.springframework.stereotype.Service;

import com.example.demo.dtos.eventos.EventoRequestDTO;
import com.example.demo.entities.eventos.Evento;
import com.example.demo.services.BaseService;

public interface EventoService extends BaseService<Evento, Long>{

    Evento crearEvento(EventoRequestDTO evento);

    Evento modificarEvento(Long id, EventoRequestDTO eventoRequestDTO);
}
