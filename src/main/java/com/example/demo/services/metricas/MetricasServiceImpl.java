package com.example.demo.services.metricas;

import java.time.LocalDateTime;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.oferta.OfertaRepository;
import com.example.demo.repositories.seguridad.UsuarioRepository;

@Service
public class MetricasServiceImpl implements MetricasService{
    private final UsuarioRepository usuarioRepository;
    private final OfertaRepository ofertaRepository;

    public MetricasServiceImpl(UsuarioRepository usuarioRepository, OfertaRepository ofertaRepository){
        this.usuarioRepository = usuarioRepository;
        this.ofertaRepository = ofertaRepository;
    }
    //ADMINITRADOR DEL SISTEMA
    @Override
    public Integer cantidadTotalHistoricaUsuarios(){
        return usuarioRepository.cantidadHistoricaUsuariosActivos();
    }

    @Override
    public Double tasaExitoOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
        Pair<LocalDateTime, LocalDateTime> fechas = manejoFechasParaFiltros(fechaDesde, fechaHasta);

        Long cantidadTotalFinalizadas = ofertaRepository.contarOfertasFinalizadas(fechas.getLeft(), fechas.getRight());
        if(cantidadTotalFinalizadas == 0){
            return 0.0;
        }
        Long cantidadTotalFinalizadasConExito = ofertaRepository.contarOfertasFinalizadasConExito(fechas.getLeft(), fechas.getRight());

        Double tasaExito = (cantidadTotalFinalizadasConExito * 100.0) / cantidadTotalFinalizadas;

        return tasaExito;
    }

    //EMPRESA

    //CANDIDATO

    //PARA FILTROS DE FECHAS
    private Pair<LocalDateTime, LocalDateTime> manejoFechasParaFiltros(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
        LocalDateTime fechaActual = LocalDateTime.now();
        //Si no se ingresa nada ==> toma el ultimo mes
        if(fechaDesde == null && fechaHasta == null){
            fechaHasta = fechaActual;
            fechaDesde = fechaHasta.minusMonths(1);
        } else if(fechaDesde != null && fechaHasta == null){ //Si se manda solo desde ==> toma desde la fecha ingresada hasta la fecha actual
            fechaHasta = fechaActual;
        } else if(fechaDesde == null && fechaHasta != null){ //Si se manda solo hasta ==> toma todo hasta la fecha ingresada
            fechaDesde = LocalDateTime.of(1800, 1, 1, 0, 0); 
        }
        if(fechaDesde != null && fechaHasta != null){
            // Validaciones
            if (fechaDesde.isAfter(fechaHasta)) {
                throw new IllegalArgumentException("La fecha desde no puede ser posterior a fecha hasta");
            }
            if (fechaDesde.isAfter(fechaActual)) {
                throw new IllegalArgumentException("La fecha desde no puede ser posterior a la fecha actual");
            }
        }

        return Pair.of(fechaDesde, fechaHasta);
    }
}