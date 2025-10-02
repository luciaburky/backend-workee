package com.example.demo.services.metricas;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.exceptions.EntityNotValidException;
import com.example.demo.repositories.seguridad.UsuarioRepository;

@Service
public class MetricasServiceImpl implements MetricasService{
    UsuarioRepository usuarioRepository;

    public MetricasServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public Integer cantidadTotalHistoricaUsuarios(){
        return usuarioRepository.cantidadHistoricaUsuariosActivos();
    }




    private Boolean validacionFiltrosCorrectos(Date fechaDesde, Date fechaHasta){
        if(fechaDesde != null){
            if(fechaHasta != null){
                if(fechaDesde.after(fechaHasta)){
                    throw new EntityNotValidException("No es posible ingresar una fecha desde mayor a la fecha hasta");
                }
            }
            if(fechaDesde.after(new Date())){
                throw new EntityNotValidException("No es posible ingresar una fecha desde mayor a la fecha actual");
            }
        }
        
        
        return true;//TODO: cambiarlo
    }
}


/*
 * 
 * No se si de esto tengo que hacer algo o si es puramente del front. Pero básicamente se tiene una fecha desde y una fecha hasta. 

Pero para lo del back:

si no selecciona la fecha desde, entonces trae toda la información hasta la fecha indicada. 

Si no selecciona la fecha hasta entonces trae toda la información desde la fecha indicada hasta la fecha actual. 

Si selecciona ambas entonces trae dentro de ese rango de fechas.
 */