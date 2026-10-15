package com.farmacia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_ingreso")
@Getter
@Setter
@NoArgsConstructor
public class DetalleIngreso extends BaseEntity {

    /** Referencia al padre (no se serializa para evitar ciclos JSON). */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull
    @Column(name = "lote_producto_id", nullable = false)
    private Long loteProductoId;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull
    @PositiveOrZero
    @Column(name = "precio_compra", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioCompra;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_producto_id", insertable = false, updatable = false)
    private LoteProducto loteProducto;
}
