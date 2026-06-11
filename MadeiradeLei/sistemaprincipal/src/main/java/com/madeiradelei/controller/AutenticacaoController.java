package com.madeiradelei.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madeiradelei.controller.dto.LoginRequest;
import com.madeiradelei.controller.dto.RegistroUsuarioRequest;
import com.madeiradelei.controller.dto.TokenResponse;
import com.madeiradelei.domain.usuario.Usuario;
import com.madeiradelei.repository.UsuarioRepository;
import com.madeiradelei.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    
    // 1. INJETAMOS O NOSSO NOVO SERVIÇO
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        
        // Verifica as credenciais no MongoDB
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 2. GERAMOS O TOKEN REAL PASSANDO O UTILIZADOR AUTENTICADO
        var tokenReal = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenReal));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistroUsuarioRequest request) {
        // Atualizado para usar o isPresent() do Optional
        if (this.repository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        String senhaEncriptada = passwordEncoder.encode(request.senha());
        Usuario novoUsuario = new Usuario(request.email(), senhaEncriptada);
        this.repository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}