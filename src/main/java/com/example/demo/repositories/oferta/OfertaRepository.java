package com.example.demo.repositories.oferta;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.oferta.Oferta;
import com.example.demo.repositories.BaseRepository;

@Repository
public interface OfertaRepository extends BaseRepository<Oferta, Long> {  

    @Query("""
        select (count(o) > 0)
        from Oferta o
          join o.ofertaEtapas oe
          join o.estadosOferta he
          join he.estadoOferta est
        where oe.etapa.id = :etapaId
          and he.fechaHoraBaja is null      
          and est.codigo <> :finalizada    
    """)
    boolean existsOfertaNoFinalizadaQueUsaEtapa(@Param("etapaId") Long etapaId,
                                                @Param("finalizada") String finalizadaCodigo);

    List<Oferta> findAllByEmpresa_IdAndFechaHoraBajaIsNull(Long empresaId);

    

}

