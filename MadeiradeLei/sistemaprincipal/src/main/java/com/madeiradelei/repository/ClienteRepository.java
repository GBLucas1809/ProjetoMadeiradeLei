package com.madeiradelei.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.madeiradelei.domain.cliente.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {
    // O Spring Boot traduz o nome do método automaticamente para uma query no MongoDB!
    Optional<Cliente> findByCnpj(String cnpj); 
}