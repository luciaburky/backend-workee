package com.example.demo.services.metricas;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.metricas.admin.DistribucionUsuariosPorRolResponseDTO;
import com.example.demo.dtos.metricas.admin.EmpresasConMasOfertasDTO;
import com.example.demo.dtos.metricas.admin.UsuariosPorPaisDTO;
import com.example.demo.dtos.metricas.admin.UsuariosPorRolDTO;
import com.example.demo.repositories.oferta.OfertaRepository;
import com.example.demo.repositories.seguridad.UsuarioRepository;
import com.example.demo.repositories.seguridad.UsuarioRolRepository;

@Service
public class MetricasServiceImpl implements MetricasService{
    private final UsuarioRepository usuarioRepository;
    private final OfertaRepository ofertaRepository;
    private final UsuarioRolRepository usuarioRolRepository;

    public MetricasServiceImpl(UsuarioRepository usuarioRepository, OfertaRepository ofertaRepository, 
    UsuarioRolRepository usuarioRolRepository){
        this.usuarioRepository = usuarioRepository;
        this.ofertaRepository = ofertaRepository;
        this.usuarioRolRepository = usuarioRolRepository;
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

    @Override
    public DistribucionUsuariosPorRolResponseDTO distribucionUsuariosPorRol(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
        Pair<LocalDateTime, LocalDateTime> fechas = manejoFechasParaFiltros(fechaDesde, fechaHasta);

        List<UsuariosPorRolDTO> usuariosPorRol = usuarioRolRepository.obtenerDistribucionUsuariosPorRol(fechas.getLeft(), fechas.getRight());
        
        Long total = usuariosPorRol.stream().mapToLong(usuario -> usuario.getCantidadUsuarios()).sum();

        for(UsuariosPorRolDTO usuario: usuariosPorRol){
            Double porcentaje = (usuario.getCantidadUsuarios() * 100.0) / total;
            usuario.setPorcentajeUsuarios(porcentaje);
        }
        DistribucionUsuariosPorRolResponseDTO distribucion = new DistribucionUsuariosPorRolResponseDTO();
        distribucion.setCantidadTotalUsuarios(total);
        distribucion.setDistribucion(usuariosPorRol);

        return distribucion;

    }

    @Override
    public List<UsuariosPorPaisDTO> cantidadUsuariosPorPaisTop5(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
        Pair<LocalDateTime, LocalDateTime> fechas = manejoFechasParaFiltros(fechaDesde, fechaHasta);
        
        List<Object[]> listado = usuarioRepository.contarUsuariosPorPaisTop5(fechas.getLeft(), fechas.getRight());
        List<UsuariosPorPaisDTO> usuariosPorPais = listado.stream()
                                                        .map(obj -> new UsuariosPorPaisDTO(
                                                                (String) obj[0],              
                                                                ((Number) obj[1]).longValue() 
                                                        ))
                                                        .collect(Collectors.toList());
        return usuariosPorPais;
    }

    @Override
    public List<EmpresasConMasOfertasDTO> topEmpresasConMasOfertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta){
        Pair<LocalDateTime, LocalDateTime> fechas = manejoFechasParaFiltros(fechaDesde, fechaHasta);

        List<EmpresasConMasOfertasDTO> empresas = ofertaRepository.topEmpresasConMasOfertas(fechas.getLeft(), fechas.getRight(), PageRequest.of(0, 5)); // primera pag, 5 elementos
        empresas.sort(Comparator.comparingLong(EmpresasConMasOfertasDTO::getCantidadOfertas));

        return empresas;
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
            if (fechaDesde.toLocalDate().isAfter(fechaActual.toLocalDate())) {
                throw new IllegalArgumentException("La fecha desde no puede ser posterior a la fecha actual");
            }
        }

        return Pair.of(fechaDesde, fechaHasta);
    }
}