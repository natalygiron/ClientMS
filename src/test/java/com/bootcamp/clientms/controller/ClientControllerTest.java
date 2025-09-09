package com.bootcamp.clientms.controller;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.service.ClientService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ClientService clientService;

    /* ============= POST /clientes ============= */
    @Test
    void create_200() {
        var req = new CreateClientRequest("Ana", "Pérez", "12345678", "ana@demo.com");

        var saved = Client.builder()
                .id("c1").firstName("Ana").lastName("Pérez")
                .dni("12345678").email("ana@demo.com").build();

        Mockito.when(clientService.register(any(CreateClientRequest.class)))
                .thenReturn(Mono.just(saved));

        webTestClient.post().uri("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk() // tu controller devuelve 200 OK (no 201)
                .expectBody()
                .jsonPath("$.id").isEqualTo("c1")
                .jsonPath("$.firstName").isEqualTo("Ana");
    }

    @Test
    void create_400_emailInvalido() {
        var req = new CreateClientRequest("Ana", "Pérez", "12345678", "correo-malo");

        webTestClient.post().uri("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isBadRequest();
    }

    /* ============= GET /clientes/{id} ============= */
    @Test
    void getById_200() {
        var found = Client.builder()
                .id("c1").firstName("Ana").lastName("Pérez")
                .dni("12345678").email("ana@demo.com").build();

        Mockito.when(clientService.getById("c1")).thenReturn(Mono.just(found));

        webTestClient.get().uri("/clientes/{id}", "c1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.dni").isEqualTo("12345678");
    }

    /* ============= GET /clientes (listAll) ============= */
    @Test
    void list_200() {
        var c1 = Client.builder().id("c1").firstName("Ana").lastName("Pérez").dni("12345678").email("ana@demo.com").build();
        var c2 = Client.builder().id("c2").firstName("Luis").lastName("Mora").dni("87654321").email("luis@demo.com").build();

        Mockito.when(clientService.listAll()).thenReturn(Flux.just(c1, c2));

        webTestClient.get().uri("/clientes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("c1")
                .jsonPath("$[1].id").isEqualTo("c2");
    }

    /* ============= PUT /clientes/{id} ============= */
    @Test
    void update_200() {
        // ← usar setters porque UpdateClientRequest no tiene constructor con args
        var req = new UpdateClientRequest();
        req.setFirstName("Ana");
        req.setLastName("Pérez");
        req.setDni("12345678");
        req.setEmail("ana@demo.com");

        var updated = Client.builder()
                .id("c1").firstName("Ana").lastName("Pérez")
                .dni("12345678").email("ana@demo.com").build();

        Mockito.when(clientService.update(eq("c1"), any(UpdateClientRequest.class)))
                .thenReturn(Mono.just(updated));

        webTestClient.put().uri("/clientes/{id}", "c1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("c1");
    }


    /* ============= PATCH /clientes/{id} ============= */
    @Test
    void patch_200() {
        var req = new PatchClientRequest();
        req.setFirstName("Ana María");

        var patched = Client.builder()
                .id("c1").firstName("Ana María").lastName("Pérez")
                .dni("12345678").email("ana@demo.com").build();

        Mockito.when(clientService.patchClient(eq("c1"), any(PatchClientRequest.class)))
                .thenReturn(Mono.just(patched));

        webTestClient.patch().uri("/clientes/{id}", "c1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Ana María");
    }

    /* ============= DELETE /clientes/{id} ============= */
    @Test
    void delete_204() {
        Mockito.when(clientService.delete("c1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/clientes/{id}", "c1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
