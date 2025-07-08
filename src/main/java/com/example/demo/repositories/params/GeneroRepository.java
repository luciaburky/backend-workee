package com.example.demo.repositories.params;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.params.Genero;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface GeneroRepository extends BaseRepository<Genero, Long> {

}
