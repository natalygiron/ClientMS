package com.bootcamp.clientms.service;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.integration.account.AccountResponse;
import com.bootcamp.clientms.repository.ClientRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;

    // ✅ WebClient con deep stubs, evita problemas con genéricos
    @Mock(answer = org.mockito.Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private ClientService clientService;

    private Client existing;

    @BeforeEach
    void setup() {
        existing = Client.builder()
                .id("c1")
                .firstName("Ana")
                .lastName("Perez")
                .dni("12345678")
                .email("ana@mail.com")
                .build();
    }

    // ✅ Versión simple con deep stubs
    private void mockAccountsServiceReturning(Flux<AccountResponse> flux) {
        given(webClient.get()
                .uri(anyString())
                .retrieve()
                .bodyToFlux(AccountResponse.class))
                .willReturn(flux);
    }

    @Nested
    @DisplayName("register")
    class RegisterTests {
        @Test
        void should_register_when_dni_and_email_free() {
            CreateClientRequest req = new CreateClientRequest();
            req.setFirstName("Juan");
            req.setLastName("Lopez");
            req.setDni("11112222");
            req.setEmail("juan@mail.com");

            given(clientRepository.existsByDni("11112222")).willReturn(Mono.just(false));
            given(clientRepository.existsByEmail("juan@mail.com")).willReturn(Mono.just(false));
            given(clientRepository.save(any(Client.class)))
                    .willAnswer(inv -> {
                        Client c = inv.getArgument(0);
                        c.setId("newId");
                        return Mono.just(c);
                    });

            StepVerifier.create(clientService.register(req))
                    .expectNextMatches(c -> c.getId().equals("newId")
                            && c.getFirstName().equals("Juan")
                            && c.getEmail().equals("juan@mail.com"))
                    .verifyComplete();

            verify(clientRepository).save(any(Client.class));
        }

        @Test
        void should_error_when_dni_in_use() {
            CreateClientRequest req = new CreateClientRequest();
            req.setFirstName("Juan");
            req.setLastName("Lopez");
            req.setDni("11112222");
            req.setEmail("juan@mail.com");

            given(clientRepository.existsByDni("11112222")).willReturn(Mono.just(true));
            given(clientRepository.existsByEmail("juan@mail.com")).willReturn(Mono.just(false));

            StepVerifier.create(clientService.register(req))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException
                            && e.getMessage().equals("DNI already in use"))
                    .verify();
        }
    }

    @Nested
    @DisplayName("getById / listAll")
    class ReadTests {
        @Test
        void should_getById_when_exists() {
            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));

            StepVerifier.create(clientService.getById("c1"))
                    .expectNext(existing)
                    .verifyComplete();
        }

        @Test
        void should_error_getById_when_not_found() {
            given(clientRepository.findById("x")).willReturn(Mono.empty());

            StepVerifier.create(clientService.getById("x"))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                            e.getMessage().equals("Client not found"))
                    .verify();
        }

        @Test
        void should_listAll() {
            given(clientRepository.findAll()).willReturn(Flux.just(existing));

            StepVerifier.create(clientService.listAll())
                    .expectNext(existing)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateTests {
        @Test
        void should_update_all_fields_and_change_email_when_free() {
            UpdateClientRequest req = new UpdateClientRequest();
            req.setFirstName("Ana Maria");
            req.setLastName("Perez Soto");
            req.setDni("87654321");
            req.setEmail("nuevo@mail.com");

            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));
            given(clientRepository.existsByEmail("nuevo@mail.com")).willReturn(Mono.just(false));
            given(clientRepository.save(any(Client.class))).willAnswer(inv -> Mono.just(inv.getArgument(0)));

            StepVerifier.create(clientService.update("c1", req))
                    .expectNextMatches(c -> c.getFirstName().equals("Ana Maria")
                            && c.getLastName().equals("Perez Soto")
                            && c.getDni().equals("87654321")
                            && c.getEmail().equals("nuevo@mail.com"))
                    .verifyComplete();
        }

        @Test
        void should_error_when_update_email_in_use() {
            UpdateClientRequest req = new UpdateClientRequest();
            req.setFirstName("Ana");
            req.setLastName("Perez");
            req.setDni("12345678");
            req.setEmail("taken@mail.com");

            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));
            given(clientRepository.existsByEmail("taken@mail.com")).willReturn(Mono.just(true));

            StepVerifier.create(clientService.update("c1", req))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                            e.getMessage().equals("Email already in use"))
                    .verify();
        }

        @Test
        void should_error_when_update_not_found() {
            UpdateClientRequest req = new UpdateClientRequest();
            req.setFirstName("X");
            req.setLastName("Y");
            req.setDni("Z");
            req.setEmail("x@y.com");

            given(clientRepository.findById("no")).willReturn(Mono.empty());

            StepVerifier.create(clientService.update("no", req))
                    .expectErrorMessage("Client not found")
                    .verify();
        }
    }

    @Nested
    @DisplayName("patchClient")
    class PatchTests {
        @Test
        void should_patch_firstName_and_dni() {
            PatchClientRequest req = new PatchClientRequest();
            req.setFirstName("Ana2");
            req.setDni("00009999");

            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));
            given(clientRepository.save(any(Client.class))).willAnswer(inv -> Mono.just(inv.getArgument(0)));

            StepVerifier.create(clientService.patchClient("c1", req))
                    .expectNextMatches(c -> c.getFirstName().equals("Ana2") && c.getDni().equals("00009999"))
                    .verifyComplete();
        }

        @Test
        void should_error_when_patch_firstName_blank() {
            PatchClientRequest req = new PatchClientRequest();
            req.setFirstName("   ");

            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));

            StepVerifier.create(clientService.patchClient("c1", req))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                            e.getMessage().equals("First name cannot be blank"))
                    .verify();
        }

        @Test
        void should_error_when_patch_email_conflict() {
            PatchClientRequest req = new PatchClientRequest();
            req.setEmail("conflict@mail.com");

            given(clientRepository.findById("c1")).willReturn(Mono.just(existing));
            given(clientRepository.existsByEmail("conflict@mail.com")).willReturn(Mono.just(true));

            StepVerifier.create(clientService.patchClient("c1", req))
                    .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                            e.getMessage().equals("Email is already in use"))
                    .verify();
        }

        @Test
        void should_error_when_patch_not_found() {
            PatchClientRequest req = new PatchClientRequest();
            req.setFirstName("X");

            given(clientRepository.findById("no")).willReturn(Mono.empty());

            StepVerifier.create(clientService.patchClient("no", req))
                    .expectErrorMessage("Client not found")
                    .verify();
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {
        @Test
        void should_error_when_has_active_accounts() {
            mockAccountsServiceReturning(Flux.just(new AccountResponse()));

            StepVerifier.create(clientService.delete("c1"))
                    .expectErrorMatches(e -> e instanceof ValidationException &&
                            e.getMessage().equals("Cannot delete client with active accounts"))
                    .verify();

            verify(clientRepository, never()).deleteById(anyString());
        }

        @Test
        void should_delete_when_no_active_accounts() {
            mockAccountsServiceReturning(Flux.empty());
            given(clientRepository.deleteById("c1")).willReturn(Mono.empty());

            StepVerifier.create(clientService.delete("c1"))
                    .verifyComplete();

            verify(clientRepository).deleteById("c1");
        }
    }
}
