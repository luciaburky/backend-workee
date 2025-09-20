package com.example.demo.dtos.eventos;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoRequestDTO {

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    private String nombreEvento;

    @NotBlank(message = "La descripción del evento no puede estar vacía")
    private String descripcionEvento;

    private Long urlVideollamada; //no se si seria el id o el url de la videollamada

    @NotBlank(message = "El tipo de evento no puede estar vacío")
    private Long idTipoEvento;

    @NotBlank(message = "La fecha y hora de inicio del evento no puede estar vacío")
    private Date fechaHoraInicioEvento; 

    private Date fechaHoraFinEvento; 

    private Long idPostulacionOfertaEtapa; //esto lo tendria que traer?
}
