package com.farmacia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_caja")
@Getter
@Setter
@NoArgsConstructor
public class MovimientoCaja extends BaseEntity {

    @NotNull
    @Column(name = "caja_id", nullable = false)
    private Long cajaId;

    @NotNull
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private String descripcion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id", insertable = false, updatable = false)
    private Caja caja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}
