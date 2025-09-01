package com.example.demo.repositories.oferta;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.oferta.Oferta;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface OfertaRepository extends BaseRepository<Oferta, Long> {  

    @Query("""
        select (count(o) > 0)
        from Oferta o
          join o.ofertaEtapas oe
          join o.estadosOferta he
          join he.estadoOferta est
        where oe.etapa.id = :etapaId
          and he.fechaHoraBaja is null      
          and est.codigo <> :finalizada    
    """)
    boolean existsOfertaNoFinalizadaQueUsaEtapa(@Param("etapaId") Long etapaId,
                                                @Param("finalizada") String finalizadaCodigo);

    List<Oferta> findAllByEmpresa_IdAndFechaHoraBajaIsNull(Long empresaId);

    
    @Query("""
        SELECT o
        FROM Oferta o
        JOIN o.estadosOferta eo
        JOIN eo.estadoOferta estado
        WHERE LOWER(o.titulo) LIKE LOWER(CONCAT('%', :nombreOferta, '%'))
        AND o.fechaFinalizacion IS NULL
        AND estado.codigo IN ('ABIERTA')
        ORDER BY o.fechaHoraAlta DESC
    """)
    public List<Oferta> buscarOfertasPorNombre(@Param("nombreOferta") String nombreOferta);

    @Query("""
        SELECT DISTINCT o
        FROM Oferta o
        JOIN o.empresa e
        JOIN e.provincia p
        JOIN o.tipoContratoOferta tc
        JOIN o.modalidadOferta mo
        JOIN o.estadosOferta eo
        JOIN eo.estadoOferta estado
        WHERE (:nombreOferta IS NULL OR LOWER(o.titulo) LIKE LOWER(CONCAT('%', :nombreOferta, '%')))
          AND (:idsProvincia IS NULL OR p.id IN :idsProvincia)
          AND (:idsTipoContrato IS NULL OR tc.id IN :idsTipoContrato)
          AND (:idsModalidadOferta IS NULL OR mo.id IN :idsModalidadOferta)
          AND (:fechaDesde IS NULL OR o.fechaHoraAlta >= :fechaDesde)
          AND eo.fechaHoraBaja IS NULL
          AND o.fechaFinalizacion IS NULL
          AND estado.codigo IN ('ABIERTA') 
          ORDER BY o.fechaHoraAlta DESC
    """)
    public List<Oferta> buscarOfertasSegunFiltros(@Param("nombreOferta") String nombreOferta,
                                                  @Param("idsProvincia") List<Long> idsProvincia,
                                                  @Param("idsTipoContrato") List<Long> idsTipoContrato,
                                                  @Param("idsModalidadOferta") List<Long> idsModalidadOferta,
                                                  @Param("fechaDesde") LocalDateTime fechaDesde);



}

