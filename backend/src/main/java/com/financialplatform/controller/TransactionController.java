package com.financialplatform.controller;

import com.financialplatform.dto.TransactionRequest;
import com.financialplatform.dto.TransactionResponse;
import com.financialplatform.dto.MonthlySpendingResponse;
import com.financialplatform.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction ledger management and business analytics metrics endpoints")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Create a new transaction", description = "Processes and records a new incoming expense or income transaction ledger item.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction recorded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request payload format supplied"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials")
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get filtered transactions", description = "Retrieves filtered, sorted, and paginated transaction records for the authenticated profile.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        /// Convert LocalDate to LocalDateTime for filtering
        LocalDateTime fromDateTime = (from != null) ? from.atStartOfDay() : null;
        LocalDateTime toDateTime = (to != null) ? to.atTime(23, 59, 59) : null;

        /// Sort transactions by showing the newest ones first
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<TransactionResponse> response = transactionService.getFilteredTransactions(category, fromDateTime, toDateTime, pageable);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieves details of a single transaction record using its numeric primary identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials"),
        @ApiResponse(responseCode = "404", description = "Transaction record could not be found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a transaction", description = "Modifies the values of an existing ledger entry item.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Invalid data payload details structural properties"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials"),
        @ApiResponse(responseCode = "404", description = "Transaction record could not be found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a transaction", description = "Removes a transaction record completely from the data layer registry using its unique ID identifier key.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "244", description = "Transaction removed successfully (No Content returned)"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials"),
        @ApiResponse(responseCode = "404", description = "Transaction record could not be found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get monthly spending analytics metrics", description = "Calculates transactional aggregation summary outlays grouped chronologically by specific year parameters.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired JWT credentials")
    })
    @GetMapping("/analytics/monthly")
    public ResponseEntity<MonthlySpendingResponse> getMonthlyAnalytics(
            @RequestParam int year,
            @RequestParam int month
    ) {
        MonthlySpendingResponse response = transactionService.calculateMonthlySpendingMetrics(year, month);
        return ResponseEntity.ok(response);
    }
}
