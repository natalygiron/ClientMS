package com.bootcamp.clientms.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Document("accounts")
public class Client {
  @Id
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String dni;
}
