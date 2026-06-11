package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrecoCustoUpdateRequest(
        @NotNull(message = "O novo preço é obrigatório") 
        @Positive(message = "O preço deve ser maior que zero") 
        BigDecimal novoPreco
) {}