package com.example.demo.entities.params;
import jakarta.persistence.Column;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_habilidad")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabilidad extends Base {
    @NotNull
    @Column(name = "nombre_tipo_habilidad")
    private String nombreTipoHabilidad;
 
    @NotNull
<<<<<<< HEAD
    @Column(name = "fecha_hora_alta_habilidad")
    private Date fechaHoraAltaTipoHabilidad;
    
    @NotNull
    @Column(name = "fecha_hora_baja_habilidad")
=======
    @Column(name = "fecha_hora_alta_tipo_habilidad")
    private Date fechaHoraAltaTipoHabilidad;
    
    @NotNull
    @Column(name = "fecha_hora_baja_tipo_habilidad")
>>>>>>> 84eaa5ec7945682cbd2f74cdd1df956678f0a100
    private Date fechaHoraBajaTipoHabilidad;
    
}
