package com.madeiradelei.domain.estoque;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import lombok.Getter;

@Document(collection = "componentes")
@Getter // Lombok apenas para leitura (Framework/DTO)
public class Componente {

    @Id
    private String id;
    private String nome;
    private UnidadeMedida unidadeMedida;
    private BigDecimal quantidadeEmEstoque;
    private BigDecimal precoCusto;

    // Construtor obriga a passar o estado inicial válido
    public Componente(String nome, UnidadeMedida unidadeMedida, BigDecimal precoCusto) {
        Assert.hasText(nome, "O nome do componente é obrigatório");
        Assert.notNull(unidadeMedida, "A unidade de medida é obrigatória");
        Assert.notNull(precoCusto, "O preço de custo é obrigatório");
        Assert.isTrue(precoCusto.compareTo(BigDecimal.ZERO) >= 0, "O preço de custo não pode ser negativo");

        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.quantidadeEmEstoque = BigDecimal.ZERO; // Todo componente nasce com estoque zerado
        this.precoCusto = precoCusto;
    }

    // --- COMPORTAMENTOS DE NEGÓCIO (Object Calisthenics) ---

    public void reporEstoque(BigDecimal quantidade) {
        Assert.notNull(quantidade, "A quantidade para reposição não pode ser nula");
        Assert.isTrue(quantidade.compareTo(BigDecimal.ZERO) > 0, "A quantidade de reposição deve ser maior que zero");
        
        this.quantidadeEmEstoque = this.quantidadeEmEstoque.add(quantidade);
    }

    public void consumirEstoque(BigDecimal quantidade) {
        Assert.notNull(quantidade, "A quantidade para consumo não pode ser nula");
        Assert.isTrue(quantidade.compareTo(BigDecimal.ZERO) > 0, "A quantidade de consumo deve ser maior que zero");
        
        // Regra de Negócio: Não permitimos estoque negativo na fábrica
        if (this.quantidadeEmEstoque.compareTo(quantidade) < 0) {
            throw new IllegalStateException("Estoque insuficiente para o componente: " + this.nome + 
                    ". Disponível: " + this.quantidadeEmEstoque + ", Solicitado: " + quantidade);
        }

        this.quantidadeEmEstoque = this.quantidadeEmEstoque.subtract(quantidade);
    }
}