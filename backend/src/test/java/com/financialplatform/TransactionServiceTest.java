package com.financialplatform.service;

import com.financialplatform.dto.TransactionRequest;
import com.financialplatform.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    /// Test for invalid requests
    @Test
    void shouldRejectInvalidTransaction() {
        TransactionRequest invalidRequest = new TransactionRequest(
            1L,                     
            "Food",                 
            BigDecimal.valueOf(-50.00), 
            "DEBIT",                
            "",                     
            LocalDateTime.now()     
        );

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.createTransaction(1L, invalidRequest);
        });
    }
}
