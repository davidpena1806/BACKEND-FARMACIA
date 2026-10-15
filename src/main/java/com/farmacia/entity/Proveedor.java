package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
public class Proveedor extends BaseEntity implements Activable {

    @NotBlank
    @Column(name = "razon_social", nullable = false, length = 200)
    private String razonSocial;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String ruc;

    @Column(name = "nombre_contacto")
    private String nombreContacto;

    private String telefono;

    private String correo;

    private String direccion;

    @Column(nullable = false)
    private Boolean activo = true;
}
