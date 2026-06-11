package com.madeiradelei.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeiradelei.controller.dto.EncomendaRequest;
import com.madeiradelei.controller.dto.EncomendaResponse;
import com.madeiradelei.controller.dto.EncomendaUpdatePagamentoRequest;
import com.madeiradelei.service.EncomendaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/encomendas")
@RequiredArgsConstructor
public class EncomendaController {

    private final EncomendaService service;

    @PostMapping
    public ResponseEntity<EncomendaResponse> criarEncomenda(@RequestBody @Valid EncomendaRequest dto) {
        EncomendaResponse criada = service.processarNovaEncomenda(
                dto.clienteId(), dto.itens(), dto.tipoPagamento(), dto.parcelas()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<EncomendaResponse>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncomendaResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}/pagamento")
    public ResponseEntity<EncomendaResponse> alterarPagamento(
            @PathVariable String id, 
            @RequestBody @Valid EncomendaUpdatePagamentoRequest request) {
        return ResponseEntity.ok(service.alterarFormaPagamento(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarEncomenda(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/iniciar-producao")
    @PreAuthorize("hasAnyRole('GERENTE', 'FUNCIONARIO')")
    public ResponseEntity<EncomendaResponse> iniciarProducao(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.iniciarProducao(id));
    }
}