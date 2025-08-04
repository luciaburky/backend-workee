package com.example.demo.entities.oferta;

import java.util.List;

import jakarta.persistence.CascadeType;

import com.example.demo.entities.Base;
import com.example.demo.entities.empresa.Empresa;
import com.example.demo.entities.params.ModalidadOferta;
import com.example.demo.entities.params.TipoContratoOferta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oferta")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Oferta extends Base {
    
    @NotBlank
    @Column(name = "titulo")
    private String titulo;
    
    @NotBlank
    @Column(name = "descripcion")
    private String descripcion;

    /* Fecha finalizacion ¿?
    private LocalDate fechaFinalizacion;
    */ 

    //String o List<String>
    @NotBlank
    @Column(name = "responsabilidades")
    private String responsabilidades;
    
    //@NotBlank 
    //¿Lo seteamos como null al inicio? 
    @Column(name = "finalizada_con_exito")
    private boolean finalizadaConExtito;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    @NotNull
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_modalidad_oferta", nullable = false)
    @NotNull
    private ModalidadOferta modalidadOferta;

    @ManyToOne()
    @JoinColumn(name = "id_tipo_contrato_oferta", nullable = false)
    @NotNull
    private TipoContratoOferta tipoContratoOferta;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_oferta", nullable = true)
    List<OfertaHabilidad> habilidades;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_oferta", nullable = false)
    private List<OfertaEstadoOferta> estadosOfertas;

    //Falta: OfertaEtapa, Estado oferta ¿? 
    
}
