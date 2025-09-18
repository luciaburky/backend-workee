package com.example.demo.repositories.eventos;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface NotificacionRepository extends BaseRepository<Notificacion, Long> {

}
