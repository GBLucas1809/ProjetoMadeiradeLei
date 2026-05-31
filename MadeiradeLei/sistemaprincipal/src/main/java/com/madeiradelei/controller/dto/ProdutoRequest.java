package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoRequest(
        @NotBlank String nome,
        @NotNull @Valid DimensoesRequest dimensoes,
        @NotNull @Min(0) BigDecimal preco,
        @NotNull @Min(1) Integer tempoFabricacaoDias
) {}