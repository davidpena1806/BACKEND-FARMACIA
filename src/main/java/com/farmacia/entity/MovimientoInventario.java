package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Bitácora de inventario: cada cambio de stock deja un registro con stock anterior y posterior. */
@Entity
@Table(name = "movimientos_inventario")
@Getter
@Setter
@NoArgsConstructor
public class MovimientoInventario extends BaseEntity {

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "lote_producto_id")
    private Long loteProductoId;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank
    @Column(name = "tipo_movimiento", nullable = false, length = 40)
    private String tipoMovimiento;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_posterior", nullable = false)
    private Integer stockPosterior;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private String referencia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_producto_id", insertable = false, updatable = false)
    private LoteProducto loteProducto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}
