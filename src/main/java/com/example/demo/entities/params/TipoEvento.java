package com.example.demo.entities.params;

import jakarta.validation.constraints.NotNull;

import com.example.demo.entities.Base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_evento")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoEvento extends Base {
    @NotNull
    @Column(name = "nombre_tipo_evento")
    private String nombreTipoEvento;

}
