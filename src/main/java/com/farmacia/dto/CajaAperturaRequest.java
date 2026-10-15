package com.farmacia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CajaAperturaRequest(
        @NotNull Long usuarioId,
        @NotNull @PositiveOrZero BigDecimal montoInicial) {
}
