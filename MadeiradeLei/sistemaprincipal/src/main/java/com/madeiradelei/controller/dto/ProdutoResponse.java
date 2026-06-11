package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        String codigo,
        String nome,
        BigDecimal preco,
        Integer tempoFabricacaoDias
) {}