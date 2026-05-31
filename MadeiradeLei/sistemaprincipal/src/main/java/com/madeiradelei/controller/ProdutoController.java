package com.madeiradelei.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeiradelei.controller.dto.ProdutoRequest;
import com.madeiradelei.controller.dto.ProdutoResponse;
import com.madeiradelei.controller.dto.ProdutoUpdateRequest;
import com.madeiradelei.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // IMPORTANTE

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    // Apenas quem tem a Role GERENTE pode criar. 
    // (Nota: O Spring automaticamente procura por "ROLE_GERENTE" no utilizador)
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')") 
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrarNovoProduto(request));
    }

    // Sem anotação: O filtro geral do SecurityConfigurations exige apenas estar autenticado com um Token válido.
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ProdutoResponse> atualizarValores(@PathVariable String id, @RequestBody @Valid ProdutoUpdateRequest request) {
        return ResponseEntity.ok(service.atualizarValores(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}