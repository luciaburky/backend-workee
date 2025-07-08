package com.example.demo.services.params;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.params.Provincia;
import com.example.demo.repositories.params.ProvinciaRepository;
import com.example.demo.services.BaseServiceImpl;

@Service
public class ProvinciaServiceImpl extends BaseServiceImpl<Provincia,Long> implements ProvinciaService{

    private final ProvinciaRepository provinciaRepository;

    public ProvinciaServiceImpl(ProvinciaRepository provinciaRepository) {
        super(provinciaRepository);
        this.provinciaRepository = provinciaRepository;
    }

    @Override
    public List<Provincia> findProvinciaByPaisId(Long idPais) {
        try{
            return provinciaRepository.findProvinciaByPaisId(idPais);
        } catch (Exception e){
            throw new RuntimeException("Error al acceder a los datos de provincias.", e);
        }
    }
}
