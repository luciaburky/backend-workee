package com.example.demo.repositories.oferta;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.postulaciones.OfertasEtapasDTO;
import com.example.demo.entities.oferta.Oferta;
import com.example.demo.entities.params.Etapa;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface OfertaRepository extends BaseRepository<Oferta, Long> {  

  @Query("""
        select (count(o) > 0)
        from Oferta o
          join o.ofertaEtapas oe
          join o.estadosOferta he
          join he.estadoOferta est
        where o.fechaHoraBaja is null                  
          and oe.etapa.id = :etapaId
          and oe.fechaHoraBaja is null                 
          and he.fechaHoraBaja is null                 
          and est.codigo <> :finalizada                
    """)
    boolean existsOfertaNoFinalizadaQueUsaEtapa(@Param("etapaId") Long etapaId,
                                                @Param("finalizada") String finalizadaCodigo);

    List<Oferta> findAllByEmpresa_IdAndFechaHoraBajaIsNull(Long empresaId);
    Boolean existsByModalidadOfertaIdAndFechaHoraBajaIsNull(Long modalidadOfertaId);
    //existsByGeneroIdAndFechaHoraBajaIsNull(idGenero)

    
    
    @Query("""
        SELECT o
        FROM Oferta o
        JOIN o.estadosOferta eo
        JOIN eo.estadoOferta estado
        WHERE LOWER(o.titulo) LIKE LOWER(CONCAT('%', :nombreOferta, '%'))
        AND eo.fechaHoraBaja IS NULL
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

    //Buscar Ofertas con Etapas asociadas a un empleado y consturir DTO 
    @Query("""
        SELECT 
            o.id,                 
            o.titulo,             
            o.descripcion,        
            est.codigo,           
            e.nombreEtapa         
        FROM Oferta o
          JOIN o.estadosOferta he
          JOIN he.estadoOferta est
          JOIN o.ofertaEtapas oe
          JOIN oe.empleadoEmpresa ee
          JOIN oe.etapa e
        WHERE
          o.fechaHoraBaja IS NULL
          AND he.fechaHoraBaja IS NULL           
          AND oe.fechaHoraBaja IS NULL           
          AND ee.id = :empleadoId
          AND est.codigo IN :codigosEstado       
        ORDER BY o.id ASC, e.nombreEtapa ASC
        """)
    List<Object[]> findOfertasEmpleado(
            @Param("empleadoId") Long empleadoId,
            @Param("codigosEstado") Collection<String> codigosEstado
    );

    //Buscar ofertas ACTIVAS/CERRADAS por empleado 
        @Query("""
        SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END
        FROM Oferta o
          JOIN o.ofertaEtapas oe
          JOIN oe.empleadoEmpresa ee
          JOIN o.estadosOferta he
          JOIN he.estadoOferta est
        WHERE
          ee.id = :empleadoId
          AND o.fechaHoraBaja IS NULL
          AND oe.fechaHoraBaja IS NULL
          AND he.fechaHoraBaja IS NULL
          AND est.codigo IN :noFinalizadas  
        """)
    boolean existsNoFinalizadaByEmpleado(
            @Param("empleadoId") Long empleadoId,
            @Param("noFinalizadas") Collection<String> noFinalizadas
    );



    @Query(
        """
        SELECT new com.example.demo.dtos.postulaciones.OfertasEtapasDTO(
          oe.id,
          e.id,
          oe.numeroEtapa,
          e.codigoEtapa,
          e.nombreEtapa
        )
        FROM Oferta o
        JOIN o.ofertaEtapas oe
        JOIN oe.etapa e
        WHERE o.id = :idOferta
        AND oe.numeroEtapa > :nroEtapa
        """
  )
  public List<OfertasEtapasDTO> buscarProximasEtapasDeOferta(@Param("idOferta") Long idOferta, @Param("nroEtapa") Integer nroEtapa);



  @Query("""
      SELECT DISTINCT o FROM Oferta o
      JOIN o.estadosOferta eo
      JOIN eo.estadoOferta e
      WHERE e.codigo = 'ABIERTA' AND eo.fechaHoraBaja IS NULL
      """)
  List<Oferta> buscarOfertasAbiertas(Long empresaId);

  @Query(value = "SELECT COUNT(DISTINCT po.id) " +
                    "FROM oferta o " +
                    "INNER JOIN postulacion_oferta AS po ON o.id = po.id_oferta " +
                    "INNER JOIN postulacion_oferta_etapa AS poe ON po.id = poe.id_postulacion_oferta " + 
                    "INNER JOIN etapa AS e ON poe.id_etapa = e.id " +
                    "WHERE o.id = :idOferta " + 
                    "AND poe.fecha_hora_baja IS NULL " +
                    "AND e.codigo_etapa NOT IN ('ABANDONADO', 'SELECCIONADO', 'RECHAZADO', 'NO_ACEPTADO')", 
    nativeQuery = true)
    public Integer obtenerCantidadDeCandidatosPostulados(@Param("idOferta") Long idOferta); 


    @Query(
      """
         SELECT e FROM Oferta o
         JOIN o.ofertaEtapas oe
         JOIN oe.etapa e
         WHERE oe.fechaHoraBaja IS NULL 
         AND e.codigoEtapa NOT IN ('PENDIENTE')
         AND o.id = :idOferta
      """
    )
    public List<Etapa> traerEtapasDeUnaOferta(@Param("idOferta") Long idOferta);

    @Query("""
          SELECT COUNT(o)
          FROM Oferta o
          WHERE o.fechaFinalizacion BETWEEN :desde AND :hasta
        """
    )
    public Long contarOfertasFinalizadas(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta );

    @Query("""
          SELECT COUNT(o)
          FROM Oferta o
          WHERE o.fechaFinalizacion BETWEEN :desde AND :hasta
          AND o.finalizadaConExito = true
        """
    )
    public Long contarOfertasFinalizadasConExito(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta );
}

