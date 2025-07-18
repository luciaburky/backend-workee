package com.example.demo.entities.candidato;

import java.util.Date;
import java.util.List;

import com.example.demo.entities.Base;
import com.example.demo.entities.params.EstadoBusqueda;
import com.example.demo.entities.params.Genero;
import com.example.demo.entities.params.Provincia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private EstadoBusqueda estadoBusqueda;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    @NotNull
    private Genero genero;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_candidato")
    private List<CandidatoHabilidad> habilidades;

    // Falta: CandidatoCV, CandidatoHabilidad, Usuario
}

