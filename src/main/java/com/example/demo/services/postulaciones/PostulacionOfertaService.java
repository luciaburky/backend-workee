package com.example.demo.services.postulaciones;

import java.util.List;

import com.example.demo.dtos.ofertas.CandidatoPostuladoDTO;
import com.example.demo.dtos.postulaciones.CambioPostulacionDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.dtos.postulaciones.PostulacionSimplificadaDTO;

public interface PostulacionOfertaService {
    public PostulacionSimplificadaDTO postularComoCandidato(PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO);

    public List<PostulacionSimplificadaDTO> obtenerPostulacionesDeUnCandidato(Long idCandidato);

    public PostulacionSimplificadaDTO abandonarPostulacionComoCandidato(Long idPostulacion);

    public PostulacionSimplificadaDTO actualizarPostulacionDeCandidato(Long idPostulacion, CambioPostulacionDTO cambioPostulacionDTO);

    public List<EtapaActualPostulacionDTO> buscarEtapasActualesDePostulacionesDeCandidato(Long idCandidato);

    public PostulacionSimplificadaDTO verDetallePostulacionDeCandidato(Long idPostulacion);

    public List<CandidatoPostuladoDTO> traerCandidatosPostuladosAOferta(Long idOferta);
}

