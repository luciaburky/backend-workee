package com.example.demo.services.metricas;

import java.util.Date;

import org.springframework.stereotype.Service;

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
        return true;
    }
}
