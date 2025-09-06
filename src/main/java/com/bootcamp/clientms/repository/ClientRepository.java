package com.bootcamp.clientms.repository;

import com.bootcamp.clientms.domain.Client;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
}
