package com.madeiradelei.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.madeiradelei.domain.estoque.Componente;

@Repository
public interface ComponenteRepository extends MongoRepository<Componente, String> {
}