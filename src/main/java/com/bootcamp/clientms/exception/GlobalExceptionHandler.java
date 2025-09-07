package com.bootcamp.clientms.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import com.bootcamp.clientms.filter.RequestPathFilter;
import com.bootcamp.clientms.dto.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<ErrorResponse> handleValidation(WebExchangeBindException ex,
      ServerWebExchange exchange) {
    String path = exchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE);

    List<String> details = ex.getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.toList());

    ErrorResponse error = new ErrorResponse(400, "Validation failed",
        LocalDateTime.now().toString(), path, ex.getClass().getSimpleName(), details);

    log.warn("Validation error at {}: {}", path, details);
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
      ServerWebExchange exchange) {
    String path = exchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE);

    ErrorResponse error = new ErrorResponse(400, ex.getMessage(), LocalDateTime.now().toString(),
        path, ex.getClass().getSimpleName(), null);

    log.warn("Illegal argument at {}: {}", path, ex.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, ServerWebExchange exchange) {
    String path = exchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE);

    ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Unexpected error: " + ex.getMessage(), LocalDateTime.now().toString(), path,
        ex.getClass().getSimpleName(), null);

    log.error("Unhandled exception: {}", ex.getMessage(), ex);
    return ResponseEntity.internalServerError().body(error);
  }
}
