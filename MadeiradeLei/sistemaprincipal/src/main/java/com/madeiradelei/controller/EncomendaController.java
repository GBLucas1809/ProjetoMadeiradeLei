package com.madeiradelei.controller;

import com.madeiradelei.controller.dto.EncomendaRequest;
import com.madeiradelei.service.CriacaoEncomendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaController {

    private final CriacaoEncomendaService service;

    public EncomendaController(CriacaoEncomendaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criarEncomenda(@RequestBody @Valid EncomendaRequest dto) {
        // O Spring valida o @RequestBody usando as anotações do EncomendaRequest.
        // Se estiver tudo certo, repassamos os dados brutos para o Service orquestrar.
        
        Object criada = service.processarNovaEncomenda(
                dto.clienteId(), 
                dto.itens(), 
                dto.tipoPagamento(), 
                dto.parcelas()
        );
        
        return ResponseEntity.ok(criada);
    }
}