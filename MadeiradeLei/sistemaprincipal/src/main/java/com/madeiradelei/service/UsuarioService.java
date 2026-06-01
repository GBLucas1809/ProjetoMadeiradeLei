package com.madeiradelei.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.madeiradelei.domain.usuario.Usuario;
import com.madeiradelei.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public void promoverUsuarioParaGerente(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilizador não encontrado"));

        // Regra de Negócio: Delega a promoção para a Entidade
        usuario.promoverParaGerente();
        
        repository.save(usuario);
    }
}