package com.financialplatform.service;

import com.financialplatform.model.Transaction;
import com.financialplatform.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
         assertThrows(IllegalArgumentException.class, () -> {
            transactionService.processTransaction("", -50.00, "Food");
        });
    }
}
