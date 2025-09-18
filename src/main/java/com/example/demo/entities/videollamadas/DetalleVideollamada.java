package com.example.demo.entities.videollamadas;

import com.example.demo.entities.Base;
import com.example.demo.entities.seguridad.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detalle_videollamada")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DetalleVideollamada extends Base{
//TODO: Preguntar si lo dejamos asi vacio o que hacemos

    @ManyToOne()
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
