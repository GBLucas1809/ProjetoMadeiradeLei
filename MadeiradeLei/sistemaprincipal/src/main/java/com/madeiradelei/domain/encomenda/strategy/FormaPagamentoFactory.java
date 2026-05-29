package com.madeiradelei.domain.encomenda.strategy;

import org.springframework.stereotype.Component;

public class FormaPagamentoFactory {

    // Retorna a estratégia correta baseada no nome recebido da API
    public static FormaPagamentoStrategy obterEstrategia(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de pagamento não pode ser nulo");
        }
        
        // No Java 14+, o 'switch expression' é enxuto, limpo e atende ao Calisthenics
        return switch (tipo.toUpperCase()) {
            case "DINHEIRO" -> new PagamentoDinheiro();
            case "PIX" -> new PagamentoPix();
            case "CREDITO" -> new PagamentoCartaoCredito();
            case "DEBITO" -> new PagamentoCartaoDebito();
            default -> throw new IllegalArgumentException("Forma de pagamento inválida: " + tipo);
        };
    }
}