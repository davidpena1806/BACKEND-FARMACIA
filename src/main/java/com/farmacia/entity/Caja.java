package com.farmacia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cajas")
@Getter
@Setter
@NoArgsConstructor
public class Caja extends BaseEntity {

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "monto_inicial", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "monto_final", precision = 18, scale = 2)
    private BigDecimal montoFinal;

    @Column(nullable = false, length = 20)
    private String estado = "ABIERTA";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}
