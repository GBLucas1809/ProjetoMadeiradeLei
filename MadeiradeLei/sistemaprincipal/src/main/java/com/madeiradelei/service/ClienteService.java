package com.madeiradelei.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.madeiradelei.controller.dto.ClienteRequest;
import com.madeiradelei.controller.dto.ClienteResponse;
import com.madeiradelei.controller.dto.ClienteUpdateRequest;
import com.madeiradelei.domain.cliente.Cliente;
import com.madeiradelei.domain.cliente.Cnpj;
import com.madeiradelei.domain.cliente.Endereco;
import com.madeiradelei.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    // --- CREATE (POST) ---
    public ClienteResponse cadastrarNovoCliente(ClienteRequest request) {
        
        // 1. Instanciamos o Value Object do Endereço usando os dados que vieram do JSON
        Endereco endereco = new Endereco(
                request.enderecoCobranca().logradouro(),
                request.enderecoCobranca().numero(),
                request.enderecoCobranca().cep(),
                request.enderecoCobranca().cidade(),
                request.enderecoCobranca().estado()
        );

        // 2. Instanciamos o Value Object do CNPJ
        Cnpj cnpj = new Cnpj(request.cnpj());

        // 3. Agora sim, passamos os 3 parâmetros exigidos pelo construtor!
        Cliente novoCliente = new Cliente(cnpj, request.razaoSocial(), endereco);
        
        Cliente clienteSalvo = repository.save(novoCliente);
        
        return mapearParaResponse(clienteSalvo);
    }

    // --- READ ALL (GET) ---
    public List<ClienteResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::mapearParaResponse)
                .toList();
    }

    // --- READ BY ID (GET) ---
    public ClienteResponse buscarPorId(String id) {
        Cliente cliente = buscarClienteNoBancoOuFalhar(id);
        return mapearParaResponse(cliente);
    }

    // --- UPDATE (PUT) ---
    public ClienteResponse atualizar(String id, ClienteUpdateRequest request) {
        Cliente cliente = buscarClienteNoBancoOuFalhar(id);
        
        // Chamamos o comportamento de domínio (Object Calisthenics)
        cliente.alterarRazaoSocial(request.razaoSocial());
        
        Cliente clienteAtualizado = repository.save(cliente);
        return mapearParaResponse(clienteAtualizado);
    }

    // --- DELETE (DELETE) ---
    public void deletar(String id) {
        Cliente cliente = buscarClienteNoBancoOuFalhar(id);
        repository.delete(cliente);
    }

    // --- MÉTODOS AUXILIARES ---
    
    private Cliente buscarClienteNoBancoOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private ClienteResponse mapearParaResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getCnpj().getValor(),
                cliente.getRazaoSocial(),
                cliente.getDataCadastramento()
        );
    }
}