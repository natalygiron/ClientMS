package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.integration.account.AccountResponse;
import com.bootcamp.clientms.repository.ClientRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

  private final ClientRepository clientRepository;
  private final WebClient webClient;

  public Mono<Client> register(CreateClientRequest req) {
    Client client = Client.builder().firstName(req.getFirstName()).lastName(req.getLastName())
        .dni(req.getDni()).email(req.getEmail()).build();

    return Mono.zip(clientRepository.existsByDni(client.getDni()),
        clientRepository.existsByEmail(client.getEmail())).flatMap(tuple -> {
          if (tuple.getT1())
            return Mono.error(new IllegalArgumentException("DNI already in use"));
          if (tuple.getT2())
            return Mono.error(new IllegalArgumentException("Email already in use"));
          return clientRepository.save(client);
        });
  }

  public Mono<Client> getById(String id) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Client not found")));
  }

  public Flux<Client> listAll() {
    return clientRepository.findAll();
  }

  public Mono<Client> update(String id, UpdateClientRequest req) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Client not found")))
        .flatMap(existing -> {
          existing.setFirstName(req.getFirstName());
          existing.setLastName(req.getLastName());
          existing.setDni(req.getDni());

          if (!existing.getEmail().equalsIgnoreCase(req.getEmail())) {
            return clientRepository.existsByEmail(req.getEmail()).flatMap(exists -> {
              if (exists)
                return Mono.error(new IllegalArgumentException("Email already in use"));
              existing.setEmail(req.getEmail());
              return Mono.just(existing);
            });
          }
          return Mono.just(existing);
        }).flatMap(clientRepository::save);
  }

  public Mono<Client> patchClient(String id, PatchClientRequest req) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Client not found")))
        .flatMap(existing -> applyPatch(existing, req)).flatMap(clientRepository::save);
  }

  public Mono<Void> delete(String id) {
    return hasActiveAccounts(id).flatMap(hasAccounts -> {
      if (hasAccounts)
        return Mono.error(new ValidationException("Cannot delete client with active accounts"));
      return clientRepository.deleteById(id);
    });
  }

  private Mono<Boolean> hasActiveAccounts(String clientId) {
    return webClient.get().uri("/cuentas?clienteId=" + clientId).retrieve()
        .bodyToFlux(AccountResponse.class).collectList().map(list -> !list.isEmpty())
        .onErrorResume(e -> Mono.error(new IllegalStateException("Accounts service unavailable")));
  }

  private Mono<Client> applyPatch(Client existing, PatchClientRequest req) {
    if (req.getFirstName() != null) {
      if (req.getFirstName().isBlank()) {
        return Mono.error(new IllegalArgumentException("First name cannot be blank"));
      }
      existing.setFirstName(req.getFirstName());
    }

    if (req.getLastName() != null) {
      if (req.getLastName().isBlank()) {
        return Mono.error(new IllegalArgumentException("Last name cannot be blank"));
      }
      existing.setLastName(req.getLastName());
    }

    if (req.getDni() != null) {
      if (req.getDni().isBlank()) {
        return Mono.error(new IllegalArgumentException("DNI cannot be blank"));
      }
      existing.setDni(req.getDni());
    }

    if (req.getEmail() != null) {
      if (req.getEmail().isBlank()) {
        return Mono.error(new IllegalArgumentException("Email cannot be blank"));
      }
      if (!req.getEmail().equalsIgnoreCase(existing.getEmail())) {
        return clientRepository.existsByEmail(req.getEmail()).flatMap(exists -> {
          if (exists) {
            return Mono.error(new IllegalArgumentException("Email is already in use"));
          }
          existing.setEmail(req.getEmail());
          return Mono.just(existing);
        });
      }
    }
    return Mono.just(existing);
  }
}
