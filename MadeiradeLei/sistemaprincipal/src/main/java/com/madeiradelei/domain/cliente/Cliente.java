package com.madeiradelei.domain.cliente;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

// O Spring Data usa a anotação @Document para mapear a classe para uma Collection
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String id; // O Mongo gera automaticamente um ObjectId
    private Cnpj cnpj; // Value Object
    private String razaoSocial;
    private Endereco enderecoCobranca; // Value Object
    private List<Endereco> enderecosEntrega; // First-class collection recomendada aqui
    private LocalDate dataCadastramento;

    // Construtor protegendo a invariância do objeto (Object Calisthenics)
    public Cliente(Cnpj cnpj, String razaoSocial, Endereco enderecoCobranca) {
        if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Razão social é obrigatória");
        }
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.enderecoCobranca = enderecoCobranca;
        this.dataCadastramento = LocalDate.now();
    }
    
    public void alterarRazaoSocial(String novaRazaoSocial) {
        Assert.hasText(novaRazaoSocial, "A nova razão social não pode estar em branco");
        this.razaoSocial = novaRazaoSocial;
    }
    
    // Comportamento ao invés de apenas 'setters'
    public void adicionarEnderecoEntrega(Endereco endereco) {
        this.enderecosEntrega.add(endereco);
    }
    
    public String getId() {
        return id;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public LocalDate getDataCadastramento() {
        return dataCadastramento;
    }
}