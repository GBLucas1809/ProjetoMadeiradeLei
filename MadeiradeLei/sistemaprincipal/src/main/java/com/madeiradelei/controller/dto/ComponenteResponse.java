package com.madeiradelei.controller.dto;

import java.math.BigDecimal;

import com.madeiradelei.domain.estoque.UnidadeMedida;

public record ComponenteResponse(
        String id,
        String nome,
        UnidadeMedida unidadeMedida,
        BigDecimal quantidadeEmEstoque,
        BigDecimal precoCusto
) {}