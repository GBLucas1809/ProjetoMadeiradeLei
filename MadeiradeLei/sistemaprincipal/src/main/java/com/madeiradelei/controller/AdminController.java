package com.madeiradelei.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeiradelei.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UsuarioService usuarioService;

    @PutMapping("/usuarios/{email}/promover-gerente")
    @PreAuthorize("hasRole('GERENTE')")
    // Altere de @PathVariable String email PARA @PathVariable("email") String email
    public ResponseEntity<Void> promoverParaGerente(@PathVariable("email") String email) {
        usuarioService.promoverUsuarioParaGerente(email);
        return ResponseEntity.noContent().build();
    }
}