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
 * PatchClientRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T01:31:22.024683600-05:00[America/Lima]", comments = "Generator version: 7.14.0")
public class PatchClientRequest {

  private @Nullable String firstName;

  private @Nullable String lastName;

  private @Nullable String dni;

  private @Nullable String email;

  public PatchClientRequest firstName(@Nullable String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @Schema(name = "firstName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public @Nullable String getFirstName() {
    return firstName;
  }

  public void setFirstName(@Nullable String firstName) {
    this.firstName = firstName;
  }

  public PatchClientRequest lastName(@Nullable String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public @Nullable String getLastName() {
    return lastName;
  }

  public void setLastName(@Nullable String lastName) {
    this.lastName = lastName;
  }

  public PatchClientRequest dni(@Nullable String dni) {
    this.dni = dni;
    return this;
  }

  /**
   * Get dni
   * @return dni
   */
  
  @Schema(name = "dni", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dni")
  public @Nullable String getDni() {
    return dni;
  }

  public void setDni(@Nullable String dni) {
    this.dni = dni;
  }

  public PatchClientRequest email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @javax.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
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
    PatchClientRequest patchClientRequest = (PatchClientRequest) o;
    return Objects.equals(this.firstName, patchClientRequest.firstName) &&
        Objects.equals(this.lastName, patchClientRequest.lastName) &&
        Objects.equals(this.dni, patchClientRequest.dni) &&
        Objects.equals(this.email, patchClientRequest.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, dni, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PatchClientRequest {\n");
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

