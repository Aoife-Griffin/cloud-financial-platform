package com.financialplatform.service;

import com.financialplatform.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class TransactionSpecifications {

    public static Specification<Transaction> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null : 
            cb.equal(cb.lower(root.get("categoryName")), category.toLowerCase());
    }

    public static Specification<Transaction> isBetweenDates(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            /// if both dates are given use in between, if only one is given use greater than or less than
            if (from != null && to != null) return cb.between(root.get("transactionDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("transactionDate"), from);
            return cb.lessThanOrEqualTo(root.get("transactionDate"), to);
        };
    }
}
