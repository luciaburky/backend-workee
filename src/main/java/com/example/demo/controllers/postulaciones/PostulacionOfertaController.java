package com.example.demo.controllers.postulaciones;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.postulaciones.CambioPostulacionDTO;
import com.example.demo.dtos.postulaciones.EtapaActualPostulacionDTO;
import com.example.demo.dtos.postulaciones.PostulacionCandidatoRequestDTO;
import com.example.demo.dtos.postulaciones.PostulacionSimplificadaDTO;
import com.example.demo.services.postulaciones.PostulacionOfertaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path ="/postulaciones")
@Tag(name = "Postulaciones", description = "Controlador para operaciones relacionadas con las postulaciones")
public class PostulacionOfertaController {
    private final PostulacionOfertaService postulacionOfertaService;

    public PostulacionOfertaController(PostulacionOfertaService postulacionOfertaService) {
        this.postulacionOfertaService = postulacionOfertaService;
    }


    @Operation(summary = "Un candidato se postula a una oferta")
    @PostMapping("")
    @PreAuthorize("hasAuthority('POSTULAR_OFERTA')")
    public ResponseEntity<?> postularAOferta(@RequestBody PostulacionCandidatoRequestDTO postulacionCandidatoRequestDTO) {
        PostulacionSimplificadaDTO postulacionOferta = postulacionOfertaService.postularComoCandidato(postulacionCandidatoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(postulacionOferta);
    }

    @Operation(summary = "Un candidato abandona una oferta")
    @PutMapping("/{idPostulacion}/abandonar")
    @PreAuthorize("hasAuthority('POSTULAR_OFERTA')")
    public ResponseEntity<?> abandonarPostulacion(@PathVariable Long idPostulacion) {
        PostulacionSimplificadaDTO postulacion = postulacionOfertaService.abandonarPostulacionComoCandidato(idPostulacion);
        return ResponseEntity.status(HttpStatus.OK).body(postulacion);
    }

    @Operation(summary = "Un candidato ve sus postulaciones")
    @GetMapping("/{idCandidato}/postulaciones")
    @PreAuthorize("hasAuthority('POSTULAR_OFERTA')") //or hasAuthority('GESTIONAR_POSTULACION')
    public ResponseEntity<?> verPostulacionesCandidato(@PathVariable Long idCandidato) {
        List<PostulacionSimplificadaDTO> postulaciones = postulacionOfertaService.obtenerPostulacionesDeUnCandidato(idCandidato);
        return ResponseEntity.status(HttpStatus.OK).body(postulaciones);
    }

    @Operation(summary = "Ver todas las etapas actuales de las postulaciones de los candidatos (para el filtro)")
    @GetMapping("/etapasActuales/{idCandidato}")
    @PreAuthorize("hasAuthority('POSTULAR_OFERTA')")
    public ResponseEntity<?> verEtapasActualesCandidato(@PathVariable Long idCandidato) {
        List<EtapaActualPostulacionDTO> etapas = postulacionOfertaService.buscarEtapasActualesDePostulacionesDeCandidato(idCandidato);
        return ResponseEntity.status(HttpStatus.OK).body(etapas);
    }


    @Operation(summary = "Actualizar la postulación de un candidato")
    @PutMapping("/{idPostulacion}")
    @PreAuthorize("hasAuthority('GESTIONAR_POSTULACION')")
    public ResponseEntity<?> actualizarPostulacionCandidato(@PathVariable Long idPostulacion, @RequestBody CambioPostulacionDTO cambioPostulacionDTO) {
        PostulacionSimplificadaDTO postulacion = postulacionOfertaService.actualizarPostulacionDeCandidato(idPostulacion, cambioPostulacionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(postulacion);
    }

    @Operation(summary = "Ver el detalle de la postulacion de un candidato")
    @GetMapping("/{idPostulacion}")
    @PreAuthorize("hasAuthority('POSTULAR_OFERTA')")
    public ResponseEntity<?> verPostulacionCandidato(@PathVariable Long idPostulacion) {
        PostulacionSimplificadaDTO postulacion = postulacionOfertaService.verDetallePostulacionDeCandidato(idPostulacion);
        return ResponseEntity.status(HttpStatus.OK).body(postulacion);
    }



}
