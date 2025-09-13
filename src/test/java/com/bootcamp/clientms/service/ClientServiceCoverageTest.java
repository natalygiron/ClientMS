package com.bootcamp.clientms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.integration.account.AccountResponse;
import com.bootcamp.clientms.repository.ClientRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de cobertura específicos para completar 100%")
class ClientServiceCoverageTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock(answer = org.mockito.Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private ClientService clientService;

    private Client existingClient;

    @BeforeEach
    void setup() {
        existingClient = Client.builder()
            .id("c1")
            .firstName("Ana")
            .lastName("Perez")
            .dni("12345678")
            .email("ana@test.com")
            .build();
    }

    @Test
    @DisplayName("Should handle WebClient error in hasActiveAccounts - lambda$hasActiveAccounts$6")
    void shouldHandleWebClientErrorInHasActiveAccounts() {
        // Arrange - Este test debe cubrir lambda$hasActiveAccounts$6(Throwable)
        // Mock WebClient para lanzar error - usando deep stubs
        given(webClient.get()
                .uri(anyString())
                .retrieve()
                .bodyToFlux(AccountResponse.class))
                .willReturn(Flux.error(new WebClientResponseException(500, "Server Error", null, null, null)));

        // Act & Assert
        StepVerifier.create(clientService.delete("c1"))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalStateException && 
                        throwable.getMessage().equals("Accounts service unavailable"))
                .verify();
    }

    @Test
    @DisplayName("Should reject blank firstName in patch - validateAndUpdateBasicFields")
    void shouldRejectBlankFirstNameInPatch() {
        // Arrange - Este test cubre una branch faltante en validateAndUpdateBasicFields
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .firstName("   ") // Blank firstName
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));

        // Act & Assert
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("First name cannot be blank"))
                .verify();
    }

    @Test
    @DisplayName("Should reject blank dni in patch - validateAndUpdateBasicFields")
    void shouldRejectBlankDniInPatch() {
        // Arrange - Este test cubre otra branch faltante en validateAndUpdateBasicFields
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .dni("") // Blank DNI
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));

        // Act & Assert
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("DNI cannot be blank"))
                .verify();
    }

    @Test
    @DisplayName("Should reject blank email in patch - validateAndUpdateEmail")
    void shouldRejectBlankEmailInPatch() {
        // Arrange - Este test cubre la branch faltante en validateAndUpdateEmail
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .email(" ") // Blank email
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));

        // Act & Assert
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("Email cannot be blank"))
                .verify();
    }

    @Test
    @DisplayName("Should handle existing email in validateAndUpdateEmail - lambda$validateAndUpdateEmail$8")
    void shouldHandleExistingEmailInValidateAndUpdateEmail() {
        // Arrange - Este test debe cubrir lambda$validateAndUpdateEmail$8
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .email("existing@test.com")
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));
        given(clientRepository.existsByEmail("existing@test.com")).willReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("Email is already in use"))
                .verify();
    }

    @Test
    @DisplayName("Should reject existing DNI in register - lambda$register$0")
    void shouldRejectExistingDniInRegister() {
        // Arrange - Este test debe cubrir lambda$register$0
        CreateClientRequest request = CreateClientRequest.builder()
                .firstName("Luis")
                .lastName("Garcia")
                .dni("87654321")
                .email("luis@test.com")
                .build();

        given(clientRepository.existsByDni("87654321")).willReturn(Mono.just(true));
        given(clientRepository.existsByEmail("luis@test.com")).willReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(clientService.register(request))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("DNI already in use"))
                .verify();
    }

    @Test
    @DisplayName("Should handle same email in update - lambda$update$2")
    void shouldHandleSameEmailInUpdate() {
        // Arrange - Este test cubre la branch faltante en lambda$update$2  
        UpdateClientRequest updateRequest = UpdateClientRequest.builder()
                .firstName("Ana")
                .lastName("Perez")
                .email("ana@test.com") // Same email as existing
                .dni("12345678") // Same DNI as existing
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));
        lenient().when(clientRepository.existsByEmail("ana@test.com")).thenReturn(Mono.just(false));
        lenient().when(clientRepository.existsByDni("12345678")).thenReturn(Mono.just(false));
        given(clientRepository.save(any(Client.class))).willReturn(Mono.just(existingClient));

        // Act & Assert - Este debería ser exitoso ya que es el mismo email
        StepVerifier.create(clientService.update("c1", updateRequest))
                .expectNextMatches(client -> 
                        client.getEmail().equals("ana@test.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should reject existing email in register - lambda$register$0 email branch")
    void shouldRejectExistingEmailInRegister() {
        // Arrange - Este test debe cubrir la branch faltante en lambda$register$0 para email ya en uso
        CreateClientRequest request = CreateClientRequest.builder()
                .firstName("Luis")
                .lastName("Garcia")
                .dni("87654321")
                .email("existing@test.com")
                .build();

        given(clientRepository.existsByDni("87654321")).willReturn(Mono.just(false));
        given(clientRepository.existsByEmail("existing@test.com")).willReturn(Mono.just(true));

        // Act & Assert - Esta es la branch faltante (línea 35)
        StepVerifier.create(clientService.register(request))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("Email already in use"))
                .verify();
    }

    @Test
    @DisplayName("Should reject blank lastName in patch - validateAndUpdateBasicFields")
    void shouldRejectBlankLastNameInPatch() {
        // Arrange - Este test cubre las branches faltantes en validateAndUpdateBasicFields (líneas 103-107)
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .lastName("   ") // Blank lastName
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));

        // Act & Assert
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectErrorMatches(throwable -> 
                        throwable instanceof IllegalArgumentException && 
                        throwable.getMessage().equals("Last name cannot be blank"))
                .verify();
    }

    @Test
    @DisplayName("Should handle same email in patch - validateAndUpdateEmail")
    void shouldHandleSameEmailInPatch() {
        // Arrange - Este test cubre la branch faltante en validateAndUpdateEmail (línea 129)
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .email("ana@test.com") // Same email as existing (case insensitive)
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));
        given(clientRepository.save(any(Client.class))).willReturn(Mono.just(existingClient));

        // Act & Assert - No debería hacer nada ya que es el mismo email
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectNextMatches(client -> 
                        client.getEmail().equals("ana@test.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should successfully update email in patch - validateAndUpdateEmail success branch")
    void shouldSuccessfullyUpdateEmailInPatch() {
        // Arrange - Este test cubre las branches faltantes en lambda$validateAndUpdateEmail$8 (líneas 134-138)
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .email("newemail@test.com") // New email
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));
        given(clientRepository.existsByEmail("newemail@test.com")).willReturn(Mono.just(false)); // Email doesn't exist
        
        Client updatedClient = Client.builder()
                .id("c1")
                .firstName("Ana")
                .lastName("Perez")
                .dni("12345678")
                .email("newemail@test.com")
                .build();
        
        given(clientRepository.save(any(Client.class))).willReturn(Mono.just(updatedClient));

        // Act & Assert - Esto cubre el branch exitoso (líneas 137-138)
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectNextMatches(client -> 
                        client.getEmail().equals("newemail@test.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should successfully update lastName in patch - validateAndUpdateBasicFields success branch")
    void shouldSuccessfullyUpdateLastNameInPatch() {
        // Arrange - Este test cubre la branch faltante en validateAndUpdateBasicFields (línea 107)
        PatchClientRequest patchRequest = PatchClientRequest.builder()
                .lastName("Nuevo Apellido") // Valid lastName
                .build();

        given(clientRepository.findById("c1")).willReturn(Mono.just(existingClient));
        
        Client updatedClient = Client.builder()
                .id("c1")
                .firstName("Ana")
                .lastName("Nuevo Apellido")
                .dni("12345678")
                .email("ana@test.com")
                .build();
        
        given(clientRepository.save(any(Client.class))).willReturn(Mono.just(updatedClient));

        // Act & Assert - Esto cubre el branch exitoso de lastName (línea 107)
        StepVerifier.create(clientService.patchClient("c1", patchRequest))
                .expectNextMatches(client -> 
                        client.getLastName().equals("Nuevo Apellido"))
                .verifyComplete();
    }
}