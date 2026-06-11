package com.madeiradelei.domain.produto;

import java.math.BigDecimal;

// Representa 1 linha da Ficha Técnica (ex: "ID do Parafuso", "10 unidades")
public record ComponenteProduto(
        String componenteId, 
        BigDecimal quantidadeNecessaria
) {}