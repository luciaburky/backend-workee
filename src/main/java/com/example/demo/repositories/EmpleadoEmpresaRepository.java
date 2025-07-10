package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.EmpleadoEmpresa;

@Repository
public interface EmpleadoEmpresaRepository extends BaseRepository<EmpleadoEmpresa, Long> {

    @Query(
        value = "SELECT COUNT(e) FROM empleado_empresa e " + 
                "WHERE e.id_empresa = :idEmpresa " + 
                "AND e.id <> :idEmpleado " + 
                "AND e.fecha_hora_baja IS NULL",
        nativeQuery = true
    )
    public Long contarOtrosEmpleadosActivos(@Param("idEmpresa") Long idEmpresa, @Param("idEmpleado") Long idEmpleado);

     @Query(
        value = "SELECT COUNT(e) FROM empleado_empresa e " + 
                "WHERE e.id_empresa = :idEmpresa " + 
                "AND e.fecha_hora_baja IS NULL",
        nativeQuery = true
    )
     public Long contarEmpelados(@Param("idEmpresa") Long idEmpresa);
}


