package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.dto.response.ClientResponse;
import com.bootcamp.clientms.repository.ClientRepository;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Client> listAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client updateClient(String id, UpdateClientRequest req) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (!req.getEmail().equalsIgnoreCase(client.getEmail()) &&
                clientRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        client.setFirstName(req.getFirstName());
        client.setLastName(req.getLastName());
        client.setEmail(req.getEmail());

        return clientRepository.save(client);
    }

    @Transactional
    public Client patchClient(String id, PatchClientRequest req) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (req.getFirstName() != null) client.setFirstName(req.getFirstName());
        if (req.getLastName() != null) client.setLastName(req.getLastName());
        if (req.getDni() != null) client.setDni(req.getDni());

        if (req.getEmail() != null && !req.getEmail().equalsIgnoreCase(client.getEmail())) {
            if (clientRepository.existsByEmail(req.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            client.setEmail(req.getEmail());
        }

        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(String id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found");
        }

        clientRepository.deleteById(id);
    }

}
