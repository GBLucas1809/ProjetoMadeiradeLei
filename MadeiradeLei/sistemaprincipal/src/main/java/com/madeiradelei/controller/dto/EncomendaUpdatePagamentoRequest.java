package com.madeiradelei.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EncomendaUpdatePagamentoRequest(
        @NotBlank(message = "O tipo de pagamento é obrigatório (ex: DINHEIRO, PIX, CREDITO)")
        String tipoPagamento,
        
        @NotNull(message = "A quantidade de parcelas é obrigatória")
        @Min(value = 1, message = "A quantidade de parcelas deve ser pelo menos 1")
        Integer parcelas
) {
}