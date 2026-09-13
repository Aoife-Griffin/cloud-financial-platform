package com.financialplatform.controller;

import com.financialplatform.dto.AccountRequest;
import com.financialplatform.dto.AccountResponse;
import com.financialplatform.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.financialplatform.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal UserPrincipal principal, 
            @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(principal.id(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<AccountResponse> response = accountService.getAllAccountsForUser(principal.id());
        return ResponseEntity.ok(response);
    }
    /// Gets an account by id
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id, 
            @AuthenticationPrincipal UserPrincipal principal) {
        AccountResponse response = accountService.getAccountByIdSecure(id, principal.id());
        return ResponseEntity.ok(response);
    }
    /// Updates account
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id, 
            @AuthenticationPrincipal UserPrincipal principal, 
            @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.updateAccountSecure(id, principal.id(), request);
        return ResponseEntity.ok(response);
    }
    /// Deletes an account by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id, 
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.deleteAccountSecure(id, principal.id());
        return ResponseEntity.noContent().build();
    }
}
