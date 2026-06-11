package com.madeiradelei.domain.encomenda.strategy;

import java.math.BigDecimal;

public class PagamentoCartaoCredito implements FormaPagamentoStrategy {
    @Override
    public BigDecimal calcularDesconto(BigDecimal valorTotal) {
        // Sem desconto
        return BigDecimal.ZERO;
    }
}