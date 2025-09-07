package com.bootcamp.clientms.repository;

import com.bootcamp.clientms.domain.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
  Mono<Boolean> existsByDni(String dni);

  Mono<Boolean> existsByEmail(String email);
}
