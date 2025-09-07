package com.bootcamp.clientms.controller;

import com.bootcamp.clientms.dto.request.CreateClientRequest;
import com.bootcamp.clientms.dto.request.PatchClientRequest;
import com.bootcamp.clientms.dto.request.UpdateClientRequest;
import com.bootcamp.clientms.dto.response.ClientResponse;
import com.bootcamp.clientms.dto.response.ErrorResponse;
import com.bootcamp.clientms.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Client", description = "Customer-related operations")
@Slf4j
@RestController
@RequestMapping("clientes")
@RequiredArgsConstructor
public class ClientController {

  private final ClientService clientService;

  @GetMapping("/{id}")
  @Operation(summary = "Obtener cliente por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado",
          content = @Content(schema = @Schema(implementation = ClientResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cliente no existe",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public Mono<ResponseEntity<ClientResponse>> get(@PathVariable String id) {
    return clientService.getById(id).map(ClientResponse::from).map(ResponseEntity::ok);
  }

  @PostMapping
  @Operation(summary = "Registrar nuevo cliente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente creado",
          content = @Content(schema = @Schema(implementation = ClientResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Email o DNI duplicado",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public Mono<ResponseEntity<ClientResponse>> create(@Valid @RequestBody CreateClientRequest req) {
    log.info("New customer registration {}", req);
    return clientService.register(req).map(ClientResponse::from).map(ResponseEntity::ok);
  }

  @GetMapping
  @Operation(summary = "Listar todos los clientes",
      description = "Devuelve una lista completa de clientes registrados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de clientes", content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = ClientResponse.class))))})
  public Flux<ClientResponse> list() {
    return clientService.listAll().map(ClientResponse::from);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar cliente por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente actualizado",
          content = @Content(schema = @Schema(implementation = ClientResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos inválidos",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cliente no existe",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Email duplicado",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public Mono<ResponseEntity<ClientResponse>> update(@PathVariable String id,
      @Valid @RequestBody UpdateClientRequest req) {
    return clientService.update(id, req).map(ClientResponse::from).map(ResponseEntity::ok);
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Actualizar parcialmente cliente por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente actualizado",
          content = @Content(schema = @Schema(implementation = ClientResponse.class))),
      @ApiResponse(responseCode = "400", description = "Error de validación",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cliente no encontrado",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "409", description = "Email ya registrado",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public Mono<ResponseEntity<ClientResponse>> patch(@PathVariable String id,
      @Valid @RequestBody PatchClientRequest req) {
    return clientService.patchClient(id, req).map(ClientResponse::from).map(ResponseEntity::ok);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Eliminar cliente por ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Cliente eliminado"),
      @ApiResponse(responseCode = "404", description = "Cliente no existe",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "400", description = "Cliente tiene cuentas activas",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return clientService.delete(id).thenReturn(ResponseEntity.noContent().build());
  }

}
