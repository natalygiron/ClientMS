package com.bootcamp.clientms.integration.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
  private String id;
  private String accountNumber;
  private BigDecimal balance;
  private AccountType type;
  private String clientId;
}
