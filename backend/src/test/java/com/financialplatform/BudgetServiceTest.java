package com.financialplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.financialplatform.dto.BudgetRequest;
import java.math.BigDecimal;
import java.time.LocalDate;



@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void shouldPreventNegativeBudget() {
        BudgetRequest negativeRequest = new BudgetRequest(
            "Food", 
            BigDecimal.valueOf(-100.00), 
            LocalDate.now(), 
            LocalDate.now().plusMonths(1)
        );
        assertThrows(IllegalArgumentException.class, () -> {
            budgetService.createBudget(negativeRequest);
        });
    }
}
