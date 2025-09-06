package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * CreateClientRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T01:31:22.024683600-05:00[America/Lima]", comments = "Generator version: 7.14.0")
public class CreateClientRequest {

  private String firstName;

  private String lastName;

  private String dni;

  private String email;

  public CreateClientRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateClientRequest(String firstName, String lastName, String dni, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dni = dni;
    this.email = email;
  }

  public CreateClientRequest firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  @NotNull 
  @Schema(name = "firstName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public CreateClientRequest lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  @NotNull 
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CreateClientRequest dni(String dni) {
    this.dni = dni;
    return this;
  }

  /**
   * Get dni
   * @return dni
   */
  @NotNull 
  @Schema(name = "dni", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dni")
  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public CreateClientRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @NotNull @javax.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateClientRequest createClientRequest = (CreateClientRequest) o;
    return Objects.equals(this.firstName, createClientRequest.firstName) &&
        Objects.equals(this.lastName, createClientRequest.lastName) &&
        Objects.equals(this.dni, createClientRequest.dni) &&
        Objects.equals(this.email, createClientRequest.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, dni, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateClientRequest {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    dni: ").append(toIndentedString(dni)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

