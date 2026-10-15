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
import java.time.LocalDate;

@Entity
@Table(name = "lotes_producto")
@Getter
@Setter
@NoArgsConstructor
public class LoteProducto extends BaseEntity implements Activable {

    @NotNull
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotBlank
    @Column(name = "numero_lote", nullable = false, length = 80)
    private String numeroLote;

    @NotNull
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stock = 0;

    @NotNull
    @PositiveOrZero
    @Column(name = "precio_compra", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioCompra;

    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;
}
