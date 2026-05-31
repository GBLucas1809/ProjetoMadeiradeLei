package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProdutoUpdateRequest(
        @NotNull @Min(0) BigDecimal preco,
        @NotNull @Min(1) Integer tempoFabricacaoDias
) {}