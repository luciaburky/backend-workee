package com.example.demo.repositories.postulaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.ofertas.CandidatoPostuladoDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
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
              AND poe.fechaHoraBaja IS NULL AND e.codigoEtapa NOT IN ('ABANDONADO', 'RECHAZADO', 'SELECCIONADO') 
        """
    )
    public Optional<PostulacionOferta> buscarPostulacionEnCurso(@Param("idCandidato") Long idCandidato, @Param("idOferta") Long idOferta); //TODO: Agregar el del codigo de un candidato que rechaza la oferta

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
            e.codigoEtapa, e.nombreEtapa
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
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
            e.codigoEtapa, e.nombreEtapa
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
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
        AND e.codigoEtapa NOT IN ('ABANDONADO', 'RECHAZADO', 'SELECCIONADO')
    """)
    List<PostulacionOferta> traerPostulacionesCandidatosEnCurso(@Param("idOferta") Long idOferta); //TODO: Agregar el estado de cuando un candidato rechaza una postulacion


     @Query("""
        SELECT DISTINCT new com.example.demo.dtos.ofertas.CandidatoPostuladoDTO(
            po.id,
            po.candidato.id, po.candidato.nombreCandidato, 
            po.candidato.apellidoCandidato, po.fechaHoraAlta, 
            e.codigoEtapa, e.nombreEtapa
        )
        FROM PostulacionOferta po
        JOIN po.postulacionOfertaEtapaList poe
        JOIN poe.etapa e
        WHERE po.oferta.id = :idOferta
        AND poe.fechaHoraBaja IS NULL
        AND e.codigoEtapa LIKE '%SELECCIONADO%'
    """)
    List<CandidatoPostuladoDTO> traerCandidatosSeleccionados(@Param("idOferta") Long idOferta); //Trae todos los candidatos seleccionados. Solo esos
}
