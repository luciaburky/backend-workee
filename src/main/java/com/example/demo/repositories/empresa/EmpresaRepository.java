package com.example.demo.repositories.empresa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.empresa.Empresa;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long>  {

    @Query(
        value = "SELECT * FROM empresa " + //TODO: Ver si agrego el nombre aca tambien en base a lo que digan en llamada
                "WHERE (:idsRubros IS NULL OR id_rubro IN (:idsRubros)) " + 
                "AND (:idsProvincias IS NULL OR id_provincia IN (:idsProvincias)) " + //TODO: Agregar el filtro de ofertas
                "AND fecha_hora_baja IS NULL",                                                                 //DUDA: deberia agregar un filtro por nombre aca? o eso lo hacemos desde el front como hicieron en parametros?
        nativeQuery = true
    )
    public List<Empresa> buscarEmpresasConFiltros(@Param("idsRubros") List<Long> idsRubros, @Param("idsProvincias") List<Long> idsProvincias);

    @Query(
        value = "SELECT * FROM empresa e " + 
                "WHERE (LOWER(e.nombre_empresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%')) ) " + 
                "AND e.fecha_hora_baja IS NULL",
        nativeQuery = true
    )
    public List<Empresa> buscarEmpresasPorNombre(@Param("nombreEmpresa") String nombreEmpresa);

    //TODO: agregar query para ver cantidad de ofertas abiertas
}

