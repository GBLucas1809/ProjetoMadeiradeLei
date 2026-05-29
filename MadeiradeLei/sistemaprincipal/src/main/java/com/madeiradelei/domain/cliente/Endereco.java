package com.madeiradelei.domain.cliente;

import org.springframework.util.Assert;

public class Endereco {

    private final String logradouro;
    private final String numero;
    private final String cep;
    private final String cidade;
    private final String estado;

    // O construtor atua como a única porta de entrada, garantindo que um Endereço 
    // nunca seja criado em um estado inválido.
    public Endereco(String logradouro, String numero, String cep, String cidade, String estado) {
        Assert.hasText(logradouro, "O logradouro é obrigatório");
        Assert.hasText(numero, "O número é obrigatório");
        
        // Aplicação de validação de domínio diretamente na entidade
        Assert.hasText(cep, "O CEP é obrigatório");
        Assert.isTrue(cep.matches("\\d{8}"), "O CEP deve conter exatamente 8 dígitos numéricos");
        
        Assert.hasText(cidade, "A cidade é obrigatória");
        Assert.hasText(estado, "O estado é obrigatório");

        this.logradouro = logradouro;
        this.numero = numero;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
    }

    // Comportamentos de domínio (evitando extrair dados para processar fora da classe)
    public String obterEnderecoFormatado() {
        return String.format("%s, %s - %s/%s (CEP: %s)", 
                this.logradouro, this.numero, this.cidade, this.estado, this.cep);
    }

    // Getters utilizados estritamente para leitura da camada de View/Service 
    // e para o mecanismo de serialização do Spring Data MongoDB
    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}