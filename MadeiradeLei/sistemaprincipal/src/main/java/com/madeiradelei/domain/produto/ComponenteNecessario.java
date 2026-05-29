package com.madeiradelei.domain.produto;

import java.math.BigDecimal;

// Representa "quantos" de um determinado componente o produto gasta
public record ComponenteNecessario(
        String componenteId, // Referência para o ID do Componente salvo no banco
        BigDecimal quantidadeNecessaria
) {
}