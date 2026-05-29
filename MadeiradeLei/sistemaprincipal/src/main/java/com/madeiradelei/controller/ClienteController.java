package com.madeiradelei.controller;

import com.madeiradelei.controller.dto.ClienteRequest;
import com.madeiradelei.controller.dto.ClienteResponse;
import com.madeiradelei.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    // Dependência injetada via construtor (nunca use @Autowired em atributos)
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@RequestBody @Valid ClienteRequest request) {
        // 1. O @Valid intercepta a requisição e devolve erro 400 (Bad Request) se falhar nas anotações do DTO.
        // 2. O Controller apenas delega a execução para o Service.
        ClienteResponse response = clienteService.cadastrarNovoCliente(request);

        // 3. Boa prática REST: Retornar HTTP 201 (Created) e o header 'Location' com a URI do novo recurso.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}