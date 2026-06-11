package com.madeiradelei.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.madeiradelei.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;

    // O Spring Security chama este método automaticamente por baixo dos panos na hora do login!
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado com o e-mail: " + username));
    }
}