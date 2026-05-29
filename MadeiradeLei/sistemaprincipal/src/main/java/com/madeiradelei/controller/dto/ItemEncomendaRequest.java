package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemEncomendaRequest(
        
        @NotBlank(message = "O código do produto é obrigatório")
        String produtoId,
        
        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}