package com.example.demo.repositories.postulaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface PostulacionOfertaRepository extends BaseRepository<PostulacionOferta, Long> {

    public Optional<PostulacionOferta> findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(Long idCandidato, Long idOferta);

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

}
