package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.entities.Empresa;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long>  {

}


