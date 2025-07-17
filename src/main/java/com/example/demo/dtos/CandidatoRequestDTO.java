package com.example.demo.dtos;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatoRequestDTO {

    @NotBlank(message = "El nombre del candidato es obligatorio")
    private String nombreCandidato;

    @NotBlank(message = "El apellido del candidato es obligatorio")
    private String apellidoCandidato;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private Date fechaDeNacimiento;

    @NotNull(message = "La provincia es obligatoria")
    private Long idProvincia;

    //Puede seleccionarlo en la creacion o posteriormente
    private Long idEstadoBusqueda;

    //Definir si es obligatorio o no
    private Long idGenero;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser un correo válido")
    private String correoCandidato;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @NotBlank(message = "Debes repetir la contraseña")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String repetirContrasenia;

    //Faltaria: CandidatoCV y CandidatoHabilidades
    
}
