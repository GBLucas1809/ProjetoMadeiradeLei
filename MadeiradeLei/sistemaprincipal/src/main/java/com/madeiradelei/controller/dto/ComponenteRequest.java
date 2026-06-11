package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

import com.madeiradelei.domain.estoque.UnidadeMedida;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponenteRequest(
        @NotBlank String nome,
        @NotNull UnidadeMedida unidadeMedida,
        @NotNull @Min(0) BigDecimal precoCusto
) {}