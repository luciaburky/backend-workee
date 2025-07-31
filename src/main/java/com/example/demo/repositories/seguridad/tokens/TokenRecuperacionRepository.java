package com.example.demo.repositories.seguridad.tokens;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.seguridad.tokens.TokenRecuperacion;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Long>{
    public Optional<TokenRecuperacion> findByToken(String token);

}
