package com.bootcamp.clientms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateClientRequest {
  @NotBlank(message = "First name is required")
  private String firstName;
  @NotBlank(message = "Last name is required")
  private String lastName;
  @NotBlank(message = "DNI is required")
  private String dni;
  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  private String email;
}
