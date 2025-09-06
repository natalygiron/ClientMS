package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.repository.ClientRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Client getById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @Transactional
    public Client register(CreateClientRequest createClientRequest) {
        Client client = Client.builder()
                .firstName(createClientRequest.getFirstName())
                .lastName(createClientRequest.getLastName())
                .email(createClientRequest.getEmail())
                .dni(createClientRequest.getDni())
                .build();

        if (isBlank(client.getFirstName()) || isBlank(client.getLastName()) ||
                isBlank(client.getDni()) || isBlank(client.getEmail())) {
            throw new ValidationException("All fields are required");
        }

        if (clientRepository.existsByDni(client.getDni())) {
            throw new IllegalArgumentException("DNI is already in use");
        }

        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        return clientRepository.save(client);
    }
}
