package com.madeiradelei.domain.encomenda.strategy;

import java.math.BigDecimal;

public class PagamentoDinheiro implements FormaPagamentoStrategy {
    @Override
    public BigDecimal calcularDesconto(BigDecimal valorTotal) {
        // 10% de desconto
        return valorTotal.multiply(new BigDecimal("0.10"));
    }
}