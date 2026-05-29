package com.madeiradelei.service;

import com.madeiradelei.controller.dto.ClienteRequest;
import com.madeiradelei.controller.dto.ClienteResponse;
import com.madeiradelei.domain.cliente.Cliente;
import com.madeiradelei.domain.cliente.Cnpj;
import com.madeiradelei.domain.cliente.Endereco;
import com.madeiradelei.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteResponse cadastrarNovoCliente(ClienteRequest request) {
        // Object Calisthenics: Transformamos os tipos primitivos (Strings) nos Value Objects do nosso domínio
        Cnpj cnpj = new Cnpj(request.cnpj());
        
        Endereco endereco = new Endereco(
                request.enderecoCobranca().logradouro(),
                request.enderecoCobranca().numero(),
                request.enderecoCobranca().cep(),
                request.enderecoCobranca().cidade(),
                request.enderecoCobranca().estado()
        );

        // Instanciamos o Agregado Raiz (A validação de regras de negócio ocorre dentro do construtor de Cliente)
        Cliente novoCliente = new Cliente(cnpj, request.razaoSocial(), endereco);

        // Persistência no MongoDB via Spring Data
        Cliente clienteSalvo = repository.save(novoCliente);

        // Retornamos mapeado para o DTO de resposta
        return new ClienteResponse(
                clienteSalvo.getId(),
                clienteSalvo.getCnpj().getValor(),
                clienteSalvo.getRazaoSocial(),
                clienteSalvo.getDataCadastramento()
        );
    }
}