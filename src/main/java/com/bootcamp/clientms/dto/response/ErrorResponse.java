package com.bootcamp.clientms.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura de error estándar")
public class ErrorResponse {

  @Schema(example = "400", description = "Código HTTP del error")
  private int status;

  @Schema(example = "DNI cannot be blank", description = "Mensaje principal del error")
  private String message;

  @Schema(example = "2025-09-06T16:45:00", description = "Fecha y hora del error")
  private String timestamp;

  @Schema(example = "/clientes/123", description = "Ruta del endpoint que falló")
  private String path;

  @Schema(example = "ValidationException", description = "Tipo de excepción lanzada")
  private String error;

  @Schema(description = "Lista de errores detallados (si aplica)")
  private List<String> details;
}
