package com.example.demo.entities.postulaciones;

import java.util.List;

import com.example.demo.entities.Base;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "postulacion_oferta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostulacionOferta extends Base{
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_postulacion_oferta")
    private List<PostulacionOfertaEtapa> postulacionOfertaEtapaList;
}
