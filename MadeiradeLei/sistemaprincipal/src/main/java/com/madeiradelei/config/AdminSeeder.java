package com.madeiradelei.config;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.madeiradelei.domain.usuario.Usuario;
import com.madeiradelei.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String emailAdmin = "admin@madeiradelei.com";

        // Verifica se o Gerente Mestre já existe para não o duplicar em cada reinício
        Optional<Usuario> adminExistente = usuarioRepository.findByEmail(emailAdmin);

        if (adminExistente.isEmpty()) {
            

            // 1. Cria o utilizador com uma senha forte
            String senhaEncriptada = passwordEncoder.encode("Admin@123");
            Usuario gerenteMestre = new Usuario(emailAdmin, senhaEncriptada);

            // 2. Chama o método de domínio que criámos anteriormente para dar os poderes!
            gerenteMestre.promoverParaGerente();

            // 3. Salva no banco de dados
            usuarioRepository.save(gerenteMestre);

            
        }
    }
}