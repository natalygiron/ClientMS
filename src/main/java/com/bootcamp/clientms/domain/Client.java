package com.bootcamp.clientms.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
  @Id
  private String id;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String dni;
}
