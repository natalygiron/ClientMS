package com.bootcamp.clientms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

import com.bootcamp.clientms.dto.response.ErrorResponse;
import com.bootcamp.clientms.filter.RequestPathFilter;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ServerWebExchange serverWebExchange;

    @Test
    @DisplayName("Should handle IllegalArgumentException correctly")
    void shouldHandleIllegalArgumentException() {
        // Given
        String path = "/clientes/123";
        String errorMessage = "Client not found";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
        
        when(serverWebExchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE)).thenReturn(path);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(exception, serverWebExchange);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertEquals("IllegalArgumentException", errorResponse.getError());
        assertNull(errorResponse.getDetails());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void shouldHandleGenericException() {
        // Given
        String path = "/clientes";
        String errorMessage = "Database connection failed";
        Exception exception = new RuntimeException(errorMessage);
        
        when(serverWebExchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE)).thenReturn(path);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception, serverWebExchange);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Unexpected error: " + errorMessage, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertEquals("RuntimeException", errorResponse.getError());
        assertNull(errorResponse.getDetails());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Should handle Exception with null path")
    void shouldHandleExceptionWithNullPath() {
        // Given
        String errorMessage = "Null pointer error";
        Exception exception = new NullPointerException(errorMessage);
        
        when(serverWebExchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE)).thenReturn(null);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception, serverWebExchange);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Unexpected error: " + errorMessage, errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertEquals("NullPointerException", errorResponse.getError());
    }

    @Test
    @DisplayName("Should handle IllegalArgumentException with null path")
    void shouldHandleIllegalArgumentExceptionWithNullPath() {
        // Given
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
        
        when(serverWebExchange.getAttribute(RequestPathFilter.PATH_ATTRIBUTE)).thenReturn(null);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgument(exception, serverWebExchange);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals(errorMessage, errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertEquals("IllegalArgumentException", errorResponse.getError());
    }
}