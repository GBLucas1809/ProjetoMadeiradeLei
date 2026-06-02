package com.madeiradelei.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.madeiradelei.controller.dto.ComponenteRequest;
import com.madeiradelei.controller.dto.ComponenteResponse;
import com.madeiradelei.controller.dto.MovimentacaoEstoqueRequest;
import com.madeiradelei.controller.dto.PrecoCustoUpdateRequest;
import com.madeiradelei.domain.estoque.Componente;
import com.madeiradelei.repository.ComponenteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComponenteService {

    private final ComponenteRepository repository;

    public ComponenteResponse cadastrar(ComponenteRequest request) {
        Componente novo = new Componente(request.nome(), request.unidadeMedida(), request.precoCusto());
        return mapearParaResponse(repository.save(novo));
    }

    public List<ComponenteResponse> listarTodos() {
        return repository.findAll().stream().map(this::mapearParaResponse).toList();
    }

    // --- MOVIMENTAÇÕES DE ESTOQUE ---

    public ComponenteResponse registrarEntrada(String id, MovimentacaoEstoqueRequest request) {
        Componente componente = buscarOuFalhar(id);
        componente.reporEstoque(request.quantidade());
        return mapearParaResponse(repository.save(componente));
    }

    public ComponenteResponse registrarSaida(String id, MovimentacaoEstoqueRequest request) {
        Componente componente = buscarOuFalhar(id);
        
        try {
            componente.consumirEstoque(request.quantidade());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
        return mapearParaResponse(repository.save(componente));
    }

    private Componente buscarOuFalhar(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Componente não encontrado"));
    }

    private ComponenteResponse mapearParaResponse(Componente c) {
        return new ComponenteResponse(c.getId(), c.getNome(), c.getUnidadeMedida(), c.getQuantidadeEmEstoque(), c.getPrecoCusto());
    }

    @Transactional
    public ComponenteResponse atualizarPrecoCusto(String id, PrecoCustoUpdateRequest request) {
        Componente componente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Componente não encontrado"));
        
        componente.atualizarPrecoDeCusto(request.novoPreco());
        
        return mapearParaResponse(repository.save(componente));
    }
}