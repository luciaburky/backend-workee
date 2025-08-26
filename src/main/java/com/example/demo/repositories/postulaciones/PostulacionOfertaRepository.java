package com.example.demo.repositories.postulaciones;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.postulaciones.PostulacionOferta;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface PostulacionOfertaRepository extends BaseRepository<PostulacionOferta, Long> {

    public Optional<PostulacionOferta> findByCandidatoIdAndOfertaIdAndFechaHoraFinPostulacionOfertaIsNull(Long idCandidato, Long idOferta);
}
