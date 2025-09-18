package com.example.demo.dtos.postulaciones;

import java.util.Date;
import java.util.List;

import com.example.demo.entities.postulaciones.PostulacionOfertaEtapa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostulacionSimplificadaDTO {
    private Date fechaHoraInicioPostulacion;
    private Date fechaHoraAbandonoOferta;
    private Date fechaHoraFinPostulacionOferta;
    private Long idIniciadorPostulacion;
    private Long idCandidato;
    private Long idPostulacionOferta;
    private Long idOferta;

    private List<PostulacionOfertaEtapa> etapas; //TODO: Ver si en vez de devolver esto, se devuelve otra cosa, pq la etapa trae a la empresa y la empresa trae el usuario, rubro, provincia, etc
}
