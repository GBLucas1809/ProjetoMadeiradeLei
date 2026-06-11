package com.madeiradelei.domain.encomenda.strategy;

import java.math.BigDecimal;

public class PagamentoPix implements FormaPagamentoStrategy {
    @Override
    public BigDecimal calcularDesconto(BigDecimal valorTotal) {
        // 5% de desconto
        return valorTotal.multiply(new BigDecimal("0.05"));
    }
}