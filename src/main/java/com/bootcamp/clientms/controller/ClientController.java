package com.bootcamp.clientms.controller;

import com.bootcamp.clientms.domain.Client;
import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.response.ClientResponse;
import com.bootcamp.clientms.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Client", description = "Customer-related operations")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("clientes")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(ClientResponse.from(clientService.getById(id)));
    }

    @Operation(summary = "Registrar nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateClientRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientService.register(request);
        log.info("New customer registration {}", request);
        return ResponseEntity.ok(ClientResponse.from(client));
    }
}
