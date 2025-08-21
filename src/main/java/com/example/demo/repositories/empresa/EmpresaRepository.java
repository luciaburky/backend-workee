package com.example.demo.repositories.empresa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.EmpresaPendienteHabilitacionDTO;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface EmpresaRepository extends BaseRepository<Empresa, Long>  {
    @Query(//TODO: Agregar el filtro por ofertas abiertas
        """ 
            SELECT e FROM Empresa e
            WHERE (:nombreEmpresa IS NULL OR LOWER(e.nombreEmpresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%')))
            AND (:idsRubros IS NULL OR e.rubro.id IN :idsRubros)
            AND (:idsProvincias IS NULL OR e.provincia.id IN :idsProvincias)
            AND e.fechaHoraBaja IS NULL        
        """
    )
    public List<Empresa> buscarEmpresasConFiltros(@Param("idsRubros") List<Long> idsRubros, @Param("idsProvincias") List<Long> idsProvincias, @Param("nombreEmpresa") String nombreEmpresa);

    @Query(
        value = "SELECT * FROM empresa e " + 
                "WHERE (LOWER(e.nombre_empresa) LIKE LOWER(CONCAT('%', :nombreEmpresa, '%')) ) " + 
                "AND e.fecha_hora_baja IS NULL",
        nativeQuery = true
    )
    public List<Empresa> buscarEmpresasPorNombre(@Param("nombreEmpresa") String nombreEmpresa);

    //TODO: agregar query para ver cantidad de ofertas abiertas

    @Query(
        """
            SELECT COUNT(e) > 0 FROM Empresa e
            JOIN e.provincia p
            JOIN p.pais pa
            WHERE e.fechaHoraBaja IS NULL AND pa.id = :idPais
        """
    )
    public boolean existenEmpresasActivasUsandoPais(@Param("idPais")Long idPais);

    boolean existsByProvinciaIdAndFechaHoraBajaIsNull(Long provinciaId);

    boolean existsByRubroIdAndFechaHoraBajaIsNull(Long rubroId);

    @Query(
        """
           SELECT new com.example.demo.dtos.EmpresaPendienteHabilitacionDTO(e.id, e.nombreEmpresa, u.urlFotoUsuario, u.correoUsuario, u.fechaHoraAlta, e.rubro)
            FROM Empresa e
            JOIN e.usuario u
            JOIN u.usuarioEstadoList ue
            JOIN ue.estadoUsuario est
            WHERE ue.fechaHoraBaja IS NULL AND e.fechaHoraBaja IS NULL
            AND LOWER(est.nombreEstadoUsuario) LIKE LOWER(:nombreEstado)     
        """
    )
    public List<EmpresaPendienteHabilitacionDTO> buscarEmpresasPendientesParaHabilitar(@Param("nombreEstado") String nombreEstado);

    Optional<Empresa> findByUsuarioId(Long usuarioId);

    Boolean existsByUsuarioId(Long usuarioId);
}

