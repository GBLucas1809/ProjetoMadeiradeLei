package com.madeiradelei.domain.encomenda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import com.madeiradelei.domain.cliente.Cliente;
import com.madeiradelei.domain.encomenda.strategy.FormaPagamentoStrategy;

import lombok.Getter;

@Document(collection = "encomendas")
@Getter // Usamos Lombok apenas para leitura, preservando o encapsulamento!
public class Encomenda {

    @Id
    private String id;
    
    // O atributo que o compilador não estava encontrando:
    private Long numero; 
    
    private LocalDate dataSolicitacao;
    
    @DBRef // Mantém a referência do cliente, mas não salva o cliente inteiro de novo
    private Cliente cliente;
    
    private List<ItemEncomenda> itens;
    
    private BigDecimal valorTotal;
    private BigDecimal valorDesconto;
    private BigDecimal valorLiquido;
    
    private FormaPagamentoStrategy estrategiaPagamento;
    private Integer parcelas;

    // Construtor principal (Fail-Fast e Object Calisthenics)
    public Encomenda(Long numero, Cliente cliente, List<ItemEncomenda> itens, FormaPagamentoStrategy estrategiaPagamento, Integer parcelas) {
        Assert.notNull(numero, "O número da encomenda é obrigatório");
        Assert.notNull(cliente, "O cliente é obrigatório");
        Assert.notEmpty(itens, "A encomenda deve ter pelo menos um item");
        Assert.notNull(estrategiaPagamento, "A estratégia de pagamento é obrigatória");
        Assert.notNull(parcelas, "A quantidade de parcelas é obrigatória");
        Assert.isTrue(parcelas > 0, "A quantidade de parcelas deve ser maior que zero");

        this.numero = numero;
        this.cliente = cliente;
        this.itens = new ArrayList<>(itens); // Protege a lista original contra modificações
        this.estrategiaPagamento = estrategiaPagamento;
        this.parcelas = parcelas;
        this.dataSolicitacao = LocalDate.now();

        // Logo após nascer com dados válidos, a encomenda já calcula seus totais
        calcularValores();
    }

    // Comportamento interno da classe
    private void calcularValores() {
        // 1. Calcula o total bruto
        this.valorTotal = this.itens.stream()
                .map(ItemEncomenda::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        // 2. Delega o cálculo do desconto para a Estratégia injetada
        this.valorDesconto = this.estrategiaPagamento.calcularDesconto(this.valorTotal);
        
        // 3. Calcula o líquido
        this.valorLiquido = this.valorTotal.subtract(this.valorDesconto);
    }

    // Protegendo a coleção de primeira classe para leitura
    public List<ItemEncomenda> getItens() {
        return Collections.unmodifiableList(this.itens);
    }
    // --- COMPORTAMENTOS DE ATUALIZAÇÃO (DDD) ---
    
    public void alterarFormaPagamento(FormaPagamentoStrategy novaEstrategia, Integer novasParcelas) {
        Assert.notNull(novaEstrategia, "A estratégia de pagamento é obrigatória");
        Assert.notNull(novasParcelas, "A quantidade de parcelas é obrigatória");
        Assert.isTrue(novasParcelas > 0, "A quantidade de parcelas deve ser maior que zero");

        this.estrategiaPagamento = novaEstrategia;
        this.parcelas = novasParcelas;
        
        // A Mágica: Ao mudar o pagamento, mandamos a encomenda recalcular seus totais!
        this.calcularValores(); 
    }
}