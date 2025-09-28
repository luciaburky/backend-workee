package com.example.demo.repositories.eventos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.eventos.Evento;
import com.example.demo.repositories.BaseRepository;

public interface EventoRepository extends BaseRepository<Evento, Long>{
    List<Evento> findByUsuarioCandidatoIdOrUsuarioEmpleadoId(Long idUsuarioCandidato, Long idUsuarioEmpleado);

    @Query("""
        SELECT e FROM Evento e
        JOIN EmpleadoEmpresa ee ON e.usuarioEmpleado.id = ee.usuario.id
        WHERE ee.empresa.id = :idEmpresa
    """)
    List<Evento> findEventosByEmpresaId(@Param("idEmpresa") Long idEmpresa);
}
