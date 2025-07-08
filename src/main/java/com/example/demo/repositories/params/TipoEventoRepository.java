package com.example.demo.repositories.params;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.TipoEvento;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface TipoEventoRepository extends BaseRepository<TipoEvento, Long> {

}
