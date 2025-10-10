package com.example.demo.repositories.postulaciones;

import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.repositories.BaseRepository;

public interface PostulacionOfertaEtapaRespository extends BaseRepository<PostulacionOfertaEtapa, Long>{
    @Query(
        """
            SELECT ee.usuario.id
            FROM PostulacionOferta po
            JOIN po.postulacionOfertaEtapaList poe
            JOIN po.oferta o
            JOIN o.ofertaEtapas oe
            JOIN oe.empleadoEmpresa ee
            WHERE poe.id = :idPostulacionOfertaEtapa
            AND oe.etapa = poe.etapa
        """
    )
    Long getIdUsuarioEmpleadoFromPOE(Long idPostulacionOfertaEtapa);
}
