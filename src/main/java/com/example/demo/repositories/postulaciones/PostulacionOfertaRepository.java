package com.example.demo.repositories.postulaciones;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO;
import com.example.demo.dtos.ofertas.CandidatoPostuladoDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.entities.params.Etapa;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface PostulacionOfertaRepository extends BaseRepository<PostulacionOferta, Long> {

    //public Optional<PostulacionOferta> findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(Long idCandidato, Long idOferta);
    
    @Query(
        """
              SELECT po FROM PostulacionOferta po
              JOIN po.postulacionOfertaEtapaList poe
              JOIN poe.etapa e
              WHERE po.candidato.id = :idCandidato AND po.oferta.id = :idOferta
              AND po.fechaHoraFinPostulacionOferta IS NULL AND po.fechaHoraAbandonoOferta IS NULL
              AND poe.fechaHoraBaja IS NULL AND e.codigoEtapa NOT IN ('ABANDONADO', 'RECHAZADO', 'SELECCIONADO', 'NO_ACEPTADO') 
        """
    )
    public Optional<PostulacionOferta> buscarPostulacionEnCurso(@Param("idCandidato") Long idCandidato, @Param("idOferta") Long idOferta);

    public List<PostulacionOferta> findByCandidatoIdOrderByFechaHoraAltaDesc(Long idCandidato);


    @Query("""
        SELECT DISTINCT new com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO(
            e.codigoEtapa, e.nombreEtapa
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        WHERE po.candidato.id = :idCandidato
        AND poe.fechaHoraBaja IS NULL
    """)
    List<EtapaActualPostulacionDTO> findEtapasActualesByCandidato(@Param("idCandidato") Long idCandidato);
    

     @Query("""
        SELECT DISTINCT new com.example.demo.dtos.ofertas.CandidatoPostuladoDTO(
            po.id,
            po.candidato.id, po.candidato.nombreCandidato, 
            po.candidato.apellidoCandidato, po.fechaHoraAlta, 
            e.codigoEtapa, e.nombreEtapa,
            u.urlFotoUsuario
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        JOIN po.candidato c
        JOIN c.usuario u
        WHERE po.oferta.id = :idOferta
        AND poe.fechaHoraBaja IS NULL
        AND e.codigoEtapa NOT IN ('PENDIENTE')
    """)
    List<CandidatoPostuladoDTO> traerCandidatosPostulados(@Param("idOferta") Long idOferta); //Trae todos los postulados excepto los pendientes

    @Query("""
        SELECT DISTINCT new com.example.demo.dtos.ofertas.CandidatoPostuladoDTO(
            po.id,
            po.candidato.id, po.candidato.nombreCandidato, 
            po.candidato.apellidoCandidato, po.fechaHoraAlta, 
            e.codigoEtapa, e.nombreEtapa,
            u.urlFotoUsuario
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        JOIN po.candidato c
        JOIN c.usuario u
        WHERE po.oferta.id = :idOferta
        AND poe.fechaHoraBaja IS NULL
        AND e.codigoEtapa LIKE '%PENDIENTE%'
        AND po.idIniciadorPostulacion <> :idEmpresa
    """)
    List<CandidatoPostuladoDTO> traerCandidatosPostuladosPendientes(@Param("idOferta") Long idOferta, @Param("idEmpresa") Long idEmpresa); //Trae solo los postulados pendientes


    @Query("""
        SELECT DISTINCT po
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        WHERE po.oferta.id = :idOferta
        AND poe.fechaHoraBaja IS NULL
        AND e.codigoEtapa NOT IN ('ABANDONADO', 'RECHAZADO', 'SELECCIONADO', 'NO_ACEPTADO')
    """)
    List<PostulacionOferta> traerPostulacionesCandidatosEnCurso(@Param("idOferta") Long idOferta); 


     @Query("""
        SELECT DISTINCT new com.example.demo.dtos.ofertas.CandidatoPostuladoDTO(
            po.id,
            po.candidato.id, po.candidato.nombreCandidato, 
            po.candidato.apellidoCandidato, po.fechaHoraAlta, 
            e.codigoEtapa, e.nombreEtapa,
            u.urlFotoUsuario
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        JOIN po.candidato c
        JOIN c.usuario u
        WHERE po.oferta.id = :idOferta
        AND poe.fechaHoraBaja IS NULL
        AND e.codigoEtapa LIKE '%SELECCIONADO%'
    """)
    List<CandidatoPostuladoDTO> traerCandidatosSeleccionados(@Param("idOferta") Long idOferta); //Trae todos los candidatos seleccionados. Solo esos


    @Query(
        """
           SELECT e FROM PostulacionOferta p
           JOIN p.postulacionOfertaEtapaList poe
           JOIN poe.etapa e
           WHERE p.oferta.id = :idOferta AND p.candidato.id = :idCandidato
           AND poe.fechaHoraBaja IS NULL
        """
    )
    Optional<Etapa> traerEtapaActualDePostulacionCandidato(@Param("idOferta") Long idOferta, @Param("idCandidato") Long idCandidato);

    @Query(
        """
              SELECT COUNT(po) FROM PostulacionOferta po
              JOIN po.postulacionOfertaEtapaList poe
              JOIN poe.etapa e
              WHERE po.candidato.id = :idCandidato 
              AND po.fechaHoraFinPostulacionOferta IS NULL AND po.fechaHoraAbandonoOferta IS NULL
              AND poe.fechaHoraBaja IS NULL AND e.codigoEtapa NOT IN ('ABANDONADO', 'RECHAZADO', 'SELECCIONADO', 'NO_ACEPTADO') 
        """
    )
    public Long traerCantidadPostulacionesEnCurso(@Param("idCandidato") Long idCandidato);


    @Query(
        """
              SELECT COUNT(po) FROM PostulacionOferta po
              JOIN po.postulacionOfertaEtapaList poe
              JOIN poe.etapa e
              WHERE po.candidato.id = :idCandidato
              AND po.fechaHoraFinPostulacionOferta IS NOT NULL
              AND poe.fechaHoraBaja IS NULL AND e.codigoEtapa LIKE 'RECHAZADO'
        """
    )
    public Long traerCantidadPostulacionesRechazadas(@Param("idCandidato") Long idCandidato);


    @Query(
        """
              SELECT new com.example.demo.dtos.metricas.candidato.RubrosDeInteresDTO(r.nombreRubro, COUNT(po))
              FROM PostulacionOferta po
              JOIN po.oferta o
              JOIN o.empresa e
              JOIN e.rubro r
              WHERE po.candidato.id = :idCandidato
              AND po.fechaHoraAlta BETWEEN :desde AND :hasta
              GROUP BY r.nombreRubro
              ORDER BY COUNT(po) DESC
        """
    )
    public List<RubrosDeInteresDTO> traerRubrosDeInteres(@Param("idCandidato") Long idCandidato, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
