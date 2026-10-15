package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria extends BaseEntity implements Activable {

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;
}
