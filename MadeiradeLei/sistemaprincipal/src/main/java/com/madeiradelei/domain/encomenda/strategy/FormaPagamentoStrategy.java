package com.madeiradelei.domain.encomenda.strategy;

import java.math.BigDecimal;

public interface FormaPagamentoStrategy {
    
    /**
     * Calcula o valor do desconto aplicado sobre o total da encomenda.
     * @param valorTotal Valor total bruto dos itens.
     * @return O valor em reais a ser descontado.
     */
    BigDecimal calcularDesconto(BigDecimal valorTotal);
}