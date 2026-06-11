package com.madeiradelei.controller.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EncomendaRequest(
        
        @NotBlank(message = "O ID do cliente é obrigatório")
        String clienteId,
        
        @NotEmpty(message = "A encomenda deve conter pelo menos um item")
        @Valid // Faz o Spring validar os campos internos de cada ItemEncomendaRequest na lista
        List<ItemEncomendaRequest> itens,
        
        @NotBlank(message = "A forma de pagamento é obrigatória (ex: DINHEIRO, PIX, CREDITO)")
        String tipoPagamento,
        
        @NotNull(message = "A quantidade de parcelas é obrigatória")
        @Min(value = 1, message = "A quantidade de parcelas deve ser pelo menos 1")
        Integer parcelas
) {
}