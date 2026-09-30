package com.financialplatform.controller;

import com.financialplatform.dto.BudgetRequest;
import com.financialplatform.dto.BudgetResponse;
import com.financialplatform.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import com.financialplatform.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @AuthenticationPrincipal UserPrincipal principal, 
            @Valid @RequestBody BudgetRequest request) {
        /// Accepts principal.id to bind the budget to the user
        BudgetResponse response = budgetService.createBudget(request); 
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(
            @AuthenticationPrincipal UserPrincipal principal) { 
        List<BudgetResponse> response = budgetService.getAllBudgetsForUser(principal.id());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id, 
            @AuthenticationPrincipal UserPrincipal principal, 
            @Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.updateBudget(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) { 
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
