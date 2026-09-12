package com.financialplatform.service;

import com.financialplatform.dto.BudgetRequest;
import com.financialplatform.dto.BudgetResponse;
import com.financialplatform.model.Budget;
import com.financialplatform.model.Transaction;
import com.financialplatform.repository.BudgetRepository;
import com.financialplatform.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(BudgetRepository budgetRepository, TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    public BudgetResponse createBudget(BudgetRequest request) {
        Budget budget = new Budget();
        budget.setName(request.name());
        budget.setAmount(request.amount());
        budget.setStartDate(request.startDate());
        budget.setEndDate(request.endDate());

        Budget saved = budgetRepository.save(budget);
        return calculateBudgetMetrics(saved);
    }

    public List<BudgetResponse> getAllBudgetsForUser(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(this::calculateBudgetMetrics)
                .collect(Collectors.toList());
    }

    public BudgetResponse updateBudget(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with ID: " + id));

        budget.setName(request.name());
        budget.setAmount(request.amount());
        budget.setStartDate(request.startDate());
        budget.setEndDate(request.endDate());

        Budget updated = budgetRepository.save(budget);
        return calculateBudgetMetrics(updated);
    }

    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new RuntimeException("Budget not found with ID: " + id);
        }
        budgetRepository.deleteById(id);
    }

    private BudgetResponse calculateBudgetMetrics(Budget budget) {
        LocalDateTime startDateTime = budget.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = budget.getEndDate().atTime(23, 59, 59);

        /// Filter transactions that are DEBIT in the time frame
        List<Transaction> explicitExpenses = transactionRepository.findAll().stream()
                .filter(tx -> "DEBIT".equalsIgnoreCase(tx.getType()))
                .filter(tx -> !tx.getTransactionDate().isBefore(startDateTime) && !tx.getTransactionDate().isAfter(endDateTime))
                .filter(tx -> budget.getName().toLowerCase().contains(tx.getCategoryName() != null ? tx.getCategoryName().toLowerCase() : "unknown"))
                .collect(Collectors.toList());

        /// Calculate total spent and remaining budget
        BigDecimal spent = explicitExpenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = budget.getAmount().subtract(spent);

        /// Calculate percentage of budget used
        BigDecimal usagePercentage = BigDecimal.ZERO;
        if (budget.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            usagePercentage = spent.divide(budget.getAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        /// Warnings for budgets
        String alertStatus = "OK";
        if (usagePercentage.compareTo(BigDecimal.valueOf(100)) >= 0) {
            alertStatus = "EXCEEDED";
        } else if (usagePercentage.compareTo(BigDecimal.valueOf(80)) >= 0) {
            alertStatus = "WARNING";
        }

        return new BudgetResponse(
                budget.getId(),
                budget.getUserId(),
                budget.getName(),
                budget.getAmount(),
                spent.setScale(2, RoundingMode.HALF_UP),
                remaining.setScale(2, RoundingMode.HALF_UP),
                usagePercentage.setScale(2, RoundingMode.HALF_UP),
                alertStatus
        );
    }
}
