package com.example.demo.entities.eventos;

import java.util.Date;

import com.example.demo.entities.Base;
import com.example.demo.entities.params.TipoEvento;
import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;
import com.example.demo.entities.videollamadas.Videollamada;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Evento extends Base{
    @Column(name = "descripcion_evento")
    private String descripcionEvento;

    @NotNull
    @Column(name = "nombre_evento")
    private String nombreEvento;
    
    @NotNull
    @Column(name = "fecha_hora_inicio_evento")
    private Date fechaHoraInicioEvento;
    
    @Column(name = "fecha_hora_fin_evento")
    private Date fechaHoraFinEvento;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) //TODO: Revisar si dejamos esto aca o si planteamos la relacion al reves, porque antes la teniamos asi pero creo que habiamos hablado de hacerla al reves...
    @JoinColumn(name = "id_videollamada", nullable = true)
    private Videollamada videollamada; 

    @ManyToOne()
    @JoinColumn(name = "id_tipo_evento", nullable = false)
    private TipoEvento tipoEvento;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "id_postulacion_oferta_etapa", nullable = false)
    private PostulacionOfertaEtapa postulacionOfertaEtapa;
}
    