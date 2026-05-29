package com.madeiradelei.domain.cliente;

import org.springframework.util.Assert;

public class Cnpj {
    
    private final String valor;

    // Construtor que garante que um CNPJ nunca nasça num estado inválido
    public Cnpj(String valor) {
        // A classe Assert do Spring é ótima para validações de domínio
        Assert.hasText(valor, "CNPJ não pode ser nulo ou vazio");
        Assert.isTrue(valor.matches("\\d{14}"), "CNPJ deve conter exatamente 14 dígitos");
        
        // Aqui entraria a lógica real de cálculo dos dígitos verificadores do CNPJ
        
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}