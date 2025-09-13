package com.bootcamp.clientms.integration.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AccountType Enum Tests")
class AccountTypeTest {

    @Test
    @DisplayName("Should have SAVINGS account type")
    void shouldHaveSavingsAccountType() {
        AccountType savings = AccountType.SAVINGS;
        assertNotNull(savings);
        assertEquals("SAVINGS", savings.name());
        assertEquals(0, savings.ordinal());
    }

    @Test
    @DisplayName("Should have CHECKING account type")
    void shouldHaveCheckingAccountType() {
        AccountType checking = AccountType.CHECKING;
        assertNotNull(checking);
        assertEquals("CHECKING", checking.name());
        assertEquals(1, checking.ordinal());
    }

    @Test
    @DisplayName("Should have exactly two account types")
    void shouldHaveExactlyTwoAccountTypes() {
        AccountType[] values = AccountType.values();
        assertEquals(2, values.length);
        assertEquals(AccountType.SAVINGS, values[0]);
        assertEquals(AccountType.CHECKING, values[1]);
    }

    @Test
    @DisplayName("Should convert string to AccountType using valueOf")
    void shouldConvertStringToAccountType() {
        assertEquals(AccountType.SAVINGS, AccountType.valueOf("SAVINGS"));
        assertEquals(AccountType.CHECKING, AccountType.valueOf("CHECKING"));
    }

    @Test
    @DisplayName("Should throw exception for invalid account type string")
    void shouldThrowExceptionForInvalidAccountType() {
        assertThrows(IllegalArgumentException.class, () -> {
            AccountType.valueOf("INVALID");
        });
    }
}