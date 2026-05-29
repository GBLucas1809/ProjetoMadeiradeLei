package com.madeiradelei.domain.produto;

import org.springframework.util.Assert;

public class Dimensoes {

    private final double altura;
    private final double largura;
    private final double profundidade;

    public Dimensoes(double altura, double largura, double profundidade) {
        // Validação de domínio: Nenhuma dimensão pode ser menor ou igual a zero
        Assert.isTrue(altura > 0, "A altura deve ser maior que zero");
        Assert.isTrue(largura > 0, "A largura deve ser maior que zero");
        Assert.isTrue(profundidade > 0, "A profundidade deve ser maior que zero");

        this.altura = altura;
        this.largura = largura;
        this.profundidade = profundidade;
    }

    // Getters para permitir que o MongoDB salve os dados
    public double getAltura() {
        return altura;
    }

    public double getLargura() {
        return largura;
    }

    public double getProfundidade() {
        return profundidade;
    }
}