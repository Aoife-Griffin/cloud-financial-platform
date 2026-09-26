package com.financialplatform.service;

import com.financialplatform.dto.TransactionRequest;
import com.financialplatform.dto.TransactionResponse;
import com.financialplatform.model.Transaction;
import com.financialplatform.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import com.financialplatform.dto.MonthlySpendingResponse;
import com.financialplatform.service.TransactionSpecifications;
import java.util.Map;
import java.util.HashMap;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        MDC.put("accountId", String.valueOf(request.accountId()));
        log.info("Attempting to create a new transaction");

        Transaction tx = new Transaction();
        tx.setAccountId(request.accountId());
        tx.setCategoryName(request.categoryName());
        tx.setAmount(request.amount());
        tx.setType(request.type().toUpperCase());
        tx.setDescription(request.description());
        if (request.transactionDate() != null) {
            tx.setTransactionDate(request.transactionDate());
        }

        Transaction saved = transactionRepository.save(tx);

        /// Create transactionId for logging
        MDC.put("transactionId", String.valueOf(saved.getId()));
        log.info("Transaction created successfully");

        return mapToResponse(saved);
    } finally {
        MDC.clear();
    }

    public Page<TransactionResponse> getFilteredTransactions(String category, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        log.info("Fetching filtered transactions for category: {}", category);
        Specification<Transaction> spec = Specification.where(TransactionSpecifications.hasCategory(category))
                .and(TransactionSpecifications.isBetweenDates(from, to));

        return transactionRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    public TransactionResponse getTransactionById(Long id) {
        try {
            MDC.put("transactionId", String.valueOf(id));
            Transaction tx = transactionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
            log.info("Transaction retrieved successfully");
            return mapToResponse(tx);
        } finally {
            MDC.clear();
        }
    }

    /// Updates an existing transaction with new details
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        try {
            MDC.put("transactionId", String.valueOf(id));
            MDC.put("accountId", String.valueOf(request.accountId()));
            log.info("Attempting to update transaction");
            Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));

            tx.setCategoryName(request.categoryName());
            tx.setAmount(request.amount());
            tx.setType(request.type().toUpperCase());
            tx.setDescription(request.description());
            if (request.transactionDate() != null) {
                tx.setTransactionDate(request.transactionDate());
        }

            Transaction updated = transactionRepository.save(tx);
            log.info("Transaction updated successfully");
            return mapToResponse(updated);
        } finally {
            MDC.clear();
        }
    }
    /// Checks if transaction exists before deleting it
    public void deleteTransaction(Long id) {
        try {
            MDC.put("transactionId", String.valueOf(id));
            log.info("Attempting to delete transaction");

            if (!transactionRepository.existsById(id)) {
                log.warn("Delete aborted: Transaction ID does not exist");
                throw new RuntimeException("Transaction not found with ID: " + id);
        } 
        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully");

        } finally {
            MDC.clear();
        }
        
        transactionRepository.deleteById(id);
    }
    /// Connects trans entity to trans response
    private TransactionResponse mapToResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getAccountId(),
                tx.getCategoryName(),
                tx.getAmount(),
                tx.getType(),
                tx.getDescription(),
                tx.getTransactionDate(),
                tx.getCreatedAt()
        );
    }
        /// Adding a method to track spendings for a given month or year
    public MonthlySpendingResponse calculateMonthlySpendingMetrics(int year, int month) {
        log.info("Calculating monthly spending metrics for year: {} month: {}", year, month);
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        /// Gets transactions for the time given
        Specification<Transaction> spec = Specification.where(TransactionSpecifications.isBetweenDates(startOfMonth, endOfMonth));
        List<Transaction> monthlyTransactions = transactionRepository.findAll(spec);

        BigDecimal totalSpending = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();

        /// Go through transactions to calculate spending and categorise
        for (Transaction tx : monthlyTransactions) {
            /// for debit transactions, should be added to the total spending
            if ("DEBIT".equalsIgnoreCase(tx.getType())) {
                BigDecimal amount = tx.getAmount();
                totalSpending = totalSpending.add(amount);

                
                String category = tx.getCategoryName() != null ? tx.getCategoryName() : "Uncategorized";
                categoryBreakdown.put(category, categoryBreakdown.getOrDefault(category, BigDecimal.ZERO).add(amount));
            }
        }

        String monthName = java.time.Month.of(month).name();
        return new MonthlySpendingResponse(monthName, year, totalSpending, categoryBreakdown);
    }

}
