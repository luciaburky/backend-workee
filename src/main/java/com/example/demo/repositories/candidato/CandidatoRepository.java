package com.example.demo.repositories.candidato;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.candidato.Candidato;
import com.example.demo.repositories.BaseRepository;

public interface CandidatoRepository extends BaseRepository<Candidato, Long> {
    
    List<Candidato> findAllByOrderByNombreCandidatoAsc();
    
    @Query("SELECT c FROM Candidato c LEFT JOIN FETCH c.habilidades ch LEFT JOIN FETCH ch.habilidad WHERE c.id = :id")
    Optional<Candidato> findByIdWithHabilidades(@Param("id") Long id);

    boolean existsByGeneroIdAndFechaHoraBajaIsNull(Long generoId);

    boolean existsByProvincia_Pais_IdAndFechaHoraBajaIsNull(Long idPais);

    boolean existsByProvinciaIdAndFechaHoraBajaIsNull(Long idProvincia);

    boolean existsByEstadoBusquedaIdAndFechaHoraBajaIsNull(Long estadoBusquedaId);

    boolean existsByHabilidades_Habilidad_IdAndFechaHoraBajaIsNull(Long habilidadId);

    boolean existsByHabilidades_Habilidad_TipoHabilidad_IdAndFechaHoraBajaIsNull(long tipoHabilidadId);

    @Query(
        value = "SELECT * FROM candidato c " + 
                "WHERE (LOWER(c.nombre_candidato) LIKE LOWER(CONCAT('%', :nombreCandidato, '%')) ) " + 
                "AND c.fecha_hora_baja IS NULL",
        nativeQuery = true
    )
    public List<Candidato> buscarCandidatosPorNombre(@Param("nombreCandidato") String nombreCandidato);


    @Query(value = """
        SELECT DISTINCT c.* FROM candidato C
        LEFT JOIN candidato_habilidad ch ON c.id = ch.id_candidato
        LEFT JOIN habilidad h ON ch.id_habilidad = h.id
        LEFT JOIN provincia p ON c.id_provincia = p.id
        WHERE (:nombreCandidato IS NULL OR LOWER(CONCAT(c.nombre_candidato, ' ', c.apellido_candidato)) LIKE LOWER(CONCAT('%', :nombreCandidato, '%'))) 
        AND (:idsEstadosBusqueda IS NULL OR c.id_estado_busqueda IN (:idsEstadosBusqueda))
        AND (:idsProvincias IS NULL OR c.id_provincia IN (:idsProvincias))
        AND (:idsPaises IS NULL OR p.id_pais IN (:idsPaises))
        AND ch.fecha_hora_baja IS NULL
        AND (:idsHabilidades IS NULL OR h.id IN (:idsHabilidades))
        AND c.fecha_hora_baja IS NULL
    """,
    nativeQuery = true)
    public List<Candidato> buscarCandidatosConFiltros(@Param("nombreCandidato") String nombreCandidato, @Param("idsHabilidades") List<Long> idsHabilidades, @Param("idsProvincias") List<Long> idsProvincias, @Param("idsPaises") List<Long> idsPaises, @Param("idsEstadosBusqueda") List<Long> idsEstadosBusqueda);
}
