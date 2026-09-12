package com.financialplatform.service;

import com.financialplatform.dto.AccountRequest;
import com.financialplatform.dto.AccountResponse;
import com.financialplatform.model.Account;
import com.financialplatform.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.financialplatform.model.Transaction;
import com.financialplatform.repository.TransactionRepository;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();
        account.setName(request.name());
        account.setType(request.type().toUpperCase());
        account.setCurrency(request.currency().toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        
        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    public AccountResponse getAccountBalanceDetails(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        /// Get all transactions
        List<Transaction> transactions = transactionRepository.findAll().stream()
                .filter(tx -> tx.getAccountId().equals(accountId))
                .collect(Collectors.toList());

        BigDecimal calculatedBalance = BigDecimal.ZERO;

        /// Calculate balance by initial + income - expenses
        for (Transaction tx : transactions) {
            if ("CREDIT".equalsIgnoreCase(tx.getType())) {
                calculatedBalance = calculatedBalance.add(tx.getAmount());
            } else if ("DEBIT".equalsIgnoreCase(tx.getType())) {
                calculatedBalance = calculatedBalance.subtract(tx.getAmount());
            }
        }

        account.setBalance(calculatedBalance);
        accountRepository.save(account); 

        return mapToResponse(account);
    }


    public List<AccountResponse> getAllAccountsForUser(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        /// Update the balance
        for (Account acc : accounts) {
            getAccountBalanceDetails(acc.getId());
        }
        return accounts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID not found: " + id));
        return getAccountBalanceDetails(id);
    }

    /// Updates an existing account with new details
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID not found: " + id));
        
        account.setName(request.name());
        account.setType(request.type().toUpperCase());
        account.setCurrency(request.currency().toUpperCase());
        
        /// Save the updated account and return the response
        Account updated = accountRepository.save(account);
        return mapToResponse(updated);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with ID: " + id);
        }
        accountRepository.deleteById(id);
    }

    /// connects account model to account response
    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getUserId(),
                account.getName(),
                account.getType(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt()
        );
    }
}
