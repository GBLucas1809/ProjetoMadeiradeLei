package com.madeiradelei.domain.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import lombok.Getter;

@Document(collection = "produtos")
@Getter // O Lombok gera apenas leitura para o DTO/MongoDB, respeitando o encapsulamento
public class Produto {

    @Id
    private String codigo;
    private String nome;
    private Dimensoes dimensoes;
    private BigDecimal preco;
    private Integer tempoFabricacaoDias;

    // Inicializamos as listas vazias para evitar NullPointerException
    private List<ComponenteNecessario> componentes = new ArrayList<>();
    private List<MaoDeObraNecessaria> horasMaoDeObra = new ArrayList<>();

    // Construtor principal blindando o nascimento do objeto
    public Produto(String nome, Dimensoes dimensoes, BigDecimal preco, Integer tempoFabricacaoDias) {
        Assert.hasText(nome, "O nome do produto é obrigatório");
        Assert.notNull(dimensoes, "As dimensões são obrigatórias");
        Assert.notNull(preco, "O preço é obrigatório");
        Assert.isTrue(preco.compareTo(BigDecimal.ZERO) >= 0, "O preço não pode ser negativo");
        Assert.notNull(tempoFabricacaoDias, "O tempo de fabricação é obrigatório");
        Assert.isTrue(tempoFabricacaoDias > 0, "O tempo de fabricação em dias deve ser maior que zero");

        this.nome = nome;
        this.dimensoes = dimensoes;
        this.preco = preco;
        this.tempoFabricacaoDias = tempoFabricacaoDias;
    }

    // --- MÉTODOS DE NEGÓCIO (COMPORTAMENTO) ---

    public void adicionarComponente(String componenteId, BigDecimal quantidade) {
        // 1. Fail-Fast: Validação rígida das entradas
        Assert.hasText(componenteId, "O ID do componente é obrigatório");
        Assert.notNull(quantidade, "A quantidade é obrigatória");
        Assert.isTrue(quantidade.compareTo(BigDecimal.ZERO) > 0, "A quantidade deve ser maior que zero");

        // 2. Regra de Negócio: Se tentarem adicionar um componente que já existe na lista, 
        // em vez de duplicar, nós apenas somamos a quantidade.
        Optional<ComponenteNecessario> existente = this.componentes.stream()
                .filter(c -> c.componenteId().equals(componenteId))
                .findFirst();

        if (existente.isPresent()) {
            BigDecimal novaQuantidade = existente.get().quantidadeNecessaria().add(quantidade);
            // Como os 'records' são imutáveis, removemos a entrada antiga e inserimos a atualizada
            this.componentes.remove(existente.get());
            this.componentes.add(new ComponenteNecessario(componenteId, novaQuantidade));
        } else {
            this.componentes.add(new ComponenteNecessario(componenteId, quantidade));
        }
    }

    public void adicionarMaoDeObra(String cargo, Integer horas) {
        // 1. Fail-Fast
        Assert.hasText(cargo, "O cargo é obrigatório");
        Assert.notNull(horas, "A quantidade de horas é obrigatória");
        Assert.isTrue(horas > 0, "A quantidade de horas deve ser maior que zero");

        // 2. Regra de Negócio: O mesmo princípio de evitar duplicidade do componente
        Optional<MaoDeObraNecessaria> existente = this.horasMaoDeObra.stream()
                .filter(m -> m.cargo().equalsIgnoreCase(cargo))
                .findFirst();

        if (existente.isPresent()) {
            Integer novasHoras = existente.get().quantidadeHoras() + horas;
            this.horasMaoDeObra.remove(existente.get());
            this.horasMaoDeObra.add(new MaoDeObraNecessaria(cargo, novasHoras));
        } else {
            this.horasMaoDeObra.add(new MaoDeObraNecessaria(cargo, horas));
        }
    }

    // --- ENCAPSULAMENTO DE COLEÇÕES (Primeira Classe) ---

    // Substituímos os Getters padrões que o Lombok geraria para as listas.
    // Retornar 'unmodifiableList' garante que ninguém de fora modifique a lista diretamente.
    public List<ComponenteNecessario> getComponentes() {
        return Collections.unmodifiableList(this.componentes);
    }

    public List<MaoDeObraNecessaria> getHorasMaoDeObra() {
        return Collections.unmodifiableList(this.horasMaoDeObra);
    }
}