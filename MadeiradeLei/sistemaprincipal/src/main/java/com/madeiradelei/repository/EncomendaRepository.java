package com.madeiradelei.repository;

import org.springframework.data.mongodb.repository.MongoRepository; 
import org.springframework.stereotype.Repository;

import com.madeiradelei.domain.encomenda.Encomenda;

@Repository
public interface EncomendaRepository extends MongoRepository<Encomenda, String> {
    // O Spring Boot implementa essa interface magicamente em tempo de execução.
    // O primeiro parâmetro (Encomenda) é o documento que será salvo.
    // O segundo parâmetro (String) é o tipo do ID do documento no MongoDB.
}