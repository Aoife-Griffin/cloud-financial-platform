package com.financialplatform.service;

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
        mockAccount.setName("Savings");
        mockAccount.setBalance(5200.00);
    }

    /// Test to make sure account is created and saved
    @Test
    void shouldCreateAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        
        Account created = accountService.createAccount("Savings", 5200.00);
        
        assertNotNull(created);
        assertEquals("Savings", created.getName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    /// Test to retrieve account
    @Test
    void shouldCalculateBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));
        
        double balance = accountService.getBalance(1L);
        
        assertEquals(5200.00, balance);
    }
}
