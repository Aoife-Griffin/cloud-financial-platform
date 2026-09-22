package com.financialplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void shouldPreventNegativeBudget() {
        assertThrows(IllegalArgumentException.class, () -> {
            budgetService.createBudget("Food", -100.00);
        });
    }
}
