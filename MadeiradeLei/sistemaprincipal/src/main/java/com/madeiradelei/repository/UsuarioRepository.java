package com.madeiradelei.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.madeiradelei.domain.usuario.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    // Retornar um Optional da classe concreta evita erros de proxy do Spring Data
    Optional<Usuario> findByEmail(String email);
}