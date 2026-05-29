package com.madeiradelei.domain.rh;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "empregados")
public class Empregado {

    @Id
    private String matricula;
    private String nome;
    private Cargo cargo; // Enum ou classe dedicada
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private List<String> qualificacoes;
    
    // Armazena apenas o ID do supervisor para evitar loops de serialização infinita
    private String supervisorId; 

    public Empregado(String matricula, String nome, BigDecimal salario, String supervisorId) {
        this.matricula = matricula;
        this.nome = nome;
        this.salario = salario;
        this.supervisorId = supervisorId;
        this.dataAdmissao = LocalDate.now();
    }

    // Métodos de negócio para gerenciar subordinação se necessário
    public boolean possuiSupervisor() {
        return this.supervisorId != null;
    }
}