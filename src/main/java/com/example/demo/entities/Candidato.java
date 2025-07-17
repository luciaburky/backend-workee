package com.example.demo.entities;

import java.util.Date;

import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.entities.params.Genero;
import com.example.demo.entities.params.Provincia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidato")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidato extends Base {
    
    @NotBlank
    @Column(name = "nombre_candidato", nullable = false, length = 50)
    @Size(max = 50)
    private String nombreCandidato;

    @NotBlank
    @Column(name = "apellido_candidato", nullable = false, length = 50)
    @Size(max = 50)
    private String apellidoCandidato;

    @NotNull
    @Column
    private Date fechaDeNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_provincia")
    @NotNull
    private Provincia provincia;
    
    @ManyToOne
    @JoinColumn(name = "id_estado_busqueda")
    @NotNull
    private EstadoBusqueda estadoBusqueda;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    @NotNull
    private Genero genero;

    // Falta: CandidatoCV, CandidatoHabilidad, Usuario
}

