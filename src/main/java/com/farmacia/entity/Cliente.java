package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends BaseEntity implements Activable {

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    private String apellido;

    private String documento;

    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento;

    private String telefono;

    private String correo;

    private String direccion;

    @Column(nullable = false)
    private Boolean activo = true;
}
