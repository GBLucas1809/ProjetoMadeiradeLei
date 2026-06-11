package com.madeiradelei.domain.rh;

import org.springframework.util.Assert;

public record Cargo(String descricao) {
    
    // Construtor compacto do Record para validação
    public Cargo {
        Assert.hasText(descricao, "A descrição do cargo é obrigatória");
        
        // Aqui você poderia adicionar outras regras futuramente, 
        // como um número mínimo/máximo de caracteres.
    }
}