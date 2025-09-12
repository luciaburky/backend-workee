package com.example.demo.services.eventos;

import org.springframework.stereotype.Service;

import com.example.demo.entities.eventos.Notificacion;
import com.example.demo.repositories.eventos.NotificacionRepository;
import com.example.demo.services.BaseServiceImpl;

import jakarta.transaction.Transactional;

@Service
public class NotificacionServiceImpl extends BaseServiceImpl<Notificacion, Long> implements NotificacionService{
    private final NotificacionRepository notificacionRepository;
    
    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        super(notificacionRepository);
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    @Transactional
    public Notificacion guardarNotificacion(Notificacion notificacion){
        return notificacionRepository.save(notificacion);
    }
}
