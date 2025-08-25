package com.example.demo.repositories.empresa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.ResultadoBusquedaEmpresaDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long>  {
    @Query(
        """ 
            SELECT new com.example.demo.dtos.ResultadoBusquedaEmpresaDTO(
                e, 
                COUNT(DISTINCT CASE 
                    WHEN (oeo.fechaHoraBaja IS NULL AND eo.codigo = 'ABIERTA') THEN o.id 
                END)
            )
            FROM Empresa e
            LEFT JOIN Oferta o ON o.empresa.id = e.id 
            LEFT JOIN o.estadosOferta oeo
            LEFT JOIN oeo.estadoOferta eo 
            WHERE (:nombreEmpresa IS NULL OR LOWER(e.nombreEmpresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%')))
            AND (:idsRubros IS NULL OR e.rubro.id IN :idsRubros)
            AND (:idsProvincias IS NULL OR e.provincia.id IN :idsProvincias)
            AND e.fechaHoraBaja IS NULL  
            AND (
                :tieneOfertasAbiertas IS NULL
                OR (
                    :tieneOfertasAbiertas = TRUE AND EXISTS (
                        SELECT 1 FROM Oferta o2 
                        JOIN o2.estadosOferta oeo2 
                        JOIN oeo2.estadoOferta eo2
                        WHERE o2.empresa.id = e.id AND oeo2.fechaHoraBaja IS NULL
                        AND eo2.codigo = 'ABIERTA'
                    )
                )
                OR (
                    :tieneOfertasAbiertas = FALSE AND NOT EXISTS (
                        SELECT 1 FROM Oferta o3 
                        JOIN o3.estadosOferta oeo3 
                        JOIN oeo3.estadoOferta eo3
                        WHERE o3.empresa.id = e.id AND oeo3.fechaHoraBaja IS NULL
                        AND eo3.codigo = 'ABIERTA'
                   )
                )
            )  
            GROUP BY e    
        """
    )
    public List<ResultadoBusquedaEmpresaDTO> buscarEmpresasConFiltros(@Param("idsRubros") List<Long> idsRubros, @Param("idsProvincias") List<Long> idsProvincias, @Param("nombreEmpresa") String nombreEmpresa, @Param("tieneOfertasAbiertas") Boolean tieneOfertasAbiertas );

    @Query(
        """
            SELECT new com.example.demo.dtos.ResultadoBusquedaEmpresaDTO(
                    e, 
                    COUNT(DISTINCT CASE 
                        WHEN (oeo.fechaHoraBaja IS NULL AND eo.codigo = 'ABIERTA') THEN o.id 
                    END)
                )
            FROM Empresa e
            LEFT JOIN Oferta o ON o.empresa.id = e.id 
            LEFT JOIN o.estadosOferta oeo
            LEFT JOIN oeo.estadoOferta eo 
            WHERE (LOWER(e.nombreEmpresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%')) ) 
            AND e.fechaHoraBaja IS NULL
            GROUP BY e
        """
    )
    public List<ResultadoBusquedaEmpresaDTO> buscarEmpresasPorNombre(@Param("nombreEmpresa") String nombreEmpresa);     

    @Query(
        """
            SELECT COUNT(e) > 0 FROM Empresa e
            JOIN e.provincia p
            JOIN p.pais pa
            WHERE e.fechaHoraBaja IS NULL AND pa.id = :idPais
        """
    )
    boolean existenEmpresasActivasUsandoPais(@Param("idPais")Long idPais);

    boolean existsByProvinciaIdAndFechaHoraBajaIsNull(Long provinciaId);

    boolean existsByRubroIdAndFechaHoraBajaIsNull(Long rubroId);
}

