package com.example.demo.repositories.eventos;

import java.util.List;

import com.example.demo.entities.eventos.Evento;
import com.example.demo.repositories.BaseRepository;

public interface EventoRepository extends BaseRepository<Evento, Long>{
    List<Evento> findByUsuarioCandidatoIdOrUsuarioEmpleadoId(Long idUsuarioCandidato, Long idUsuarioEmpleado);
    
}
