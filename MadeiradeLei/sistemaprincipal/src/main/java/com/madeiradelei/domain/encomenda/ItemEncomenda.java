package com.madeiradelei.domain.encomenda;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.util.Assert;

// O record garante que o item da encomenda seja imutável após ser criado
public record ItemEncomenda(
        String produtoId,
        Integer quantidade,
        BigDecimal precoUnitario, // Fotografia do preço no momento da compra
        LocalDate dataEntrega
) {
    // Construtor compacto para Fail-Fast (Validação)
    public ItemEncomenda {
        Assert.hasText(produtoId, "O ID do produto é obrigatório");
        
        Assert.notNull(quantidade, "A quantidade é obrigatória");
        Assert.isTrue(quantidade > 0, "A quantidade deve ser maior que zero");
        
        Assert.notNull(precoUnitario, "O preço unitário é obrigatório");
        Assert.isTrue(precoUnitario.compareTo(BigDecimal.ZERO) > 0, "O preço unitário não pode ser negativo ou zero");
        
        Assert.notNull(dataEntrega, "A data de entrega é obrigatória");
    }

    // --- COMPORTAMENTO (Object Calisthenics) ---
    // A própria classe sabe calcular seu subtotal, evitando lógica espalhada no Service
    public BigDecimal calcularSubtotal() {
        return this.precoUnitario.multiply(new BigDecimal(this.quantidade));
    }
}