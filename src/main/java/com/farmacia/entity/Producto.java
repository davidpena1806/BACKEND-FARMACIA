package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto extends BaseEntity implements Activable {

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    private String descripcion;

    @Column(name = "principio_activo")
    private String principioActivo;

    private String laboratorio;

    private String presentacion;

    @NotBlank
    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @NotNull
    @PositiveOrZero
    @Column(name = "precio_venta", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioVenta;

    @NotNull
    @PositiveOrZero
    @Column(name = "precio_compra_referencial", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioCompraReferencial;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stock = 0;

    @NotNull
    @PositiveOrZero
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 0;

    @Column(name = "requiere_receta", nullable = false)
    private Boolean requiereReceta = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @NotNull
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    /** Navegación de solo lectura: la categoría se asigna únicamente mediante categoriaId. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
    private Categoria categoria;
}
