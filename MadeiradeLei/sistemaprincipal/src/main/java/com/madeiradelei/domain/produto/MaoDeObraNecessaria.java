package com.madeiradelei.domain.produto;

// Representa quantas horas de um cargo específico são necessárias
public record MaoDeObraNecessaria(
        String cargo,
        Integer quantidadeHoras
) {
}