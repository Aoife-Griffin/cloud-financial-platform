package com.financialplatform.service;

import com.financialplatform.dto.AccountRequest;
import com.financialplatform.dto.AccountResponse;

import com.financialplatform.model.Account;
import com.financialplatform.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account mockAccount;

    @BeforeEach
    void setUp() {
        mockAccount = new Account();
        mockAccount.setId(1L);
        mockAccount.setUserId(1L);
        mockAccount.setName("Savings");
        mockAccount.setBalance(BigDecimal.valueOf(5200.00));;
    }

    /// Test to make sure account is created and saved
    @Test
    void shouldCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        
        AccountRequest request = new AccountRequest("Savings", "123456789", "CHECKING");
        AccountResponse response = accountService.createAccount(1L, request);
        
        assertNotNull(response);
        assertEquals("Savings", response.name());
        assertEquals(BigDecimal.valueOf(5200.00), response.balance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    /// Test to retrieve secure account by ID and verify its properties
    @Test
    void shouldGetAccountByIdSecure() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        AccountResponse response = accountService.getAccountByIdSecure(1L, 1L);
        
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(5200.00), response.balance());
    }
}
