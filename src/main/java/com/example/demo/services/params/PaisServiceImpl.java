package com.example.demo.services.params;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Pais;
import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.PaisRepository;
import com.example.demo.services.BaseServiceImpl;


@Service
public class PaisServiceImpl extends BaseServiceImpl<Pais,Long> implements PaisService{
    private final PaisRepository paisRepository;

    private final ProvinciaService provinciaService;

    public PaisServiceImpl(PaisRepository paisRepository, ProvinciaService provinciaService) {
        super(paisRepository);
        this.paisRepository = paisRepository;
        this.provinciaService = provinciaService;
    }

    // public List<Provincia> getProvinciasByPaisId(Long paisId) {
    //     try{
    //         List<Provincia> provincias = provinciaService.findProvinciaByPaisId(paisId);
    //         return provincias;
    //     } catch (Exception e){
    //         throw new RuntimeException("Error al acceder a los datos de provincias.", e);
    //     }
    // }
}
