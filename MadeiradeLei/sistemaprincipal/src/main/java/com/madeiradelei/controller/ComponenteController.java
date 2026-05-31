package com.madeiradelei.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeiradelei.controller.dto.ComponenteRequest;
import com.madeiradelei.controller.dto.ComponenteResponse;
import com.madeiradelei.controller.dto.MovimentacaoEstoqueRequest;
import com.madeiradelei.service.ComponenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/componentes")
@RequiredArgsConstructor
public class ComponenteController {

    private final ComponenteService service;

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@RequestBody @Valid ComponenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(request));
    }

    @GetMapping
    public ResponseEntity<List<ComponenteResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}/entrada")
    @PreAuthorize("hasAnyRole('GERENTE', 'FUNCIONARIO')") // Duas roles permitidas
    public ResponseEntity<ComponenteResponse> entradaEstoque(@PathVariable String id, @RequestBody @Valid MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(service.registrarEntrada(id, request));
    }

    // A saída pode ser acionada manualmente por Funcionários (ex: uma peça quebrou e foi para o lixo)
    @PutMapping("/{id}/saida")
    @PreAuthorize("hasAnyRole('GERENTE', 'FUNCIONARIO')")
    public ResponseEntity<ComponenteResponse> saidaEstoque(@PathVariable String id, @RequestBody @Valid MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(service.registrarSaida(id, request));
    }
}