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
            return Mono.error(new IllegalArgumentException("El DNI ya está registrado"));
          if (tuple.getT2())
            return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado"));
          return clientRepository.save(client);
        });
  }

  public Mono<Client> getById(String id) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente no encontrado")));
  }

  public Flux<Client> listAll() {
    return clientRepository.findAll();
  }

  public Mono<Client> update(String id, UpdateClientRequest req) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente no encontrado")))
        .flatMap(existing -> {
          existing.setFirstName(req.getFirstName());
          existing.setLastName(req.getLastName());
          existing.setDni(req.getDni());

          if (!existing.getEmail().equalsIgnoreCase(req.getEmail())) {
            return clientRepository.existsByEmail(req.getEmail()).flatMap(exists -> {
              if (exists)
                return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado"));
              existing.setEmail(req.getEmail());
              return Mono.just(existing);
            });
          }
          return Mono.just(existing);
        }).flatMap(clientRepository::save);
  }

  public Mono<Client> patchClient(String id, PatchClientRequest req) {
    return clientRepository.findById(id)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Cliente no encontrado")))
        .flatMap(existing -> applyPatch(existing, req)).flatMap(clientRepository::save);
  }

  public Mono<Void> delete(String id) {
    return hasActiveAccounts(id).flatMap(hasAccounts -> {
      if (hasAccounts)
        return Mono.error(new ValidationException("No se puede eliminar el cliente porque tiene cuentas activas"));
      return clientRepository.deleteById(id);
    });
  }

  private Mono<Boolean> hasActiveAccounts(String clientId) {
    return webClient.get().uri("/cuentas?clienteId=" + clientId).retrieve()
            .bodyToFlux(AccountResponse.class)
            .filter(account -> clientId.equals(account.getClientId()))
            .collectList()
            .map(list -> !list.isEmpty())
            .onErrorResume(e -> Mono.error(new IllegalStateException("El servicio de AccountMS no está disponible")));
  }

  private Mono<Client> applyPatch(Client existing, PatchClientRequest req) {
    if (req.getFirstName() != null) {
      if (req.getFirstName().isBlank()) {
        return Mono.error(new IllegalArgumentException("El nombre no puede estar vacío"));
      }
      existing.setFirstName(req.getFirstName());
    }

    if (req.getLastName() != null) {
      if (req.getLastName().isBlank()) {
        return Mono.error(new IllegalArgumentException("El apellido no puede estar vacío"));
      }
      existing.setLastName(req.getLastName());
    }

    if (req.getDni() != null) {
      if (req.getDni().isBlank()) {
        return Mono.error(new IllegalArgumentException("El DNI no puede estar vacío"));
      }
      existing.setDni(req.getDni());
    }

    if (req.getEmail() != null) {
      if (req.getEmail().isBlank()) {
        return Mono.error(new IllegalArgumentException("El correo electrónico no puede estar vacío"));
      }
      if (!req.getEmail().equalsIgnoreCase(existing.getEmail())) {
        return clientRepository.existsByEmail(req.getEmail()).flatMap(exists -> {
          if (exists) {
            return Mono.error(new IllegalArgumentException("El correo electrónico ya está registrado"));
          }
          existing.setEmail(req.getEmail());
          return Mono.just(existing);
        });
      }
    }
    return Mono.just(existing);
  }
}
