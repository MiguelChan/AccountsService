package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.exceptions.InvalidRequestException;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import java.util.HashSet;
import java.util.Set;

/** Validates write payloads before invoking transactional components. */
public final class AccountInputValidator {

    private AccountInputValidator() {
    }

    /** Validates the required fields and rejects duplicate child updates. */
    public static void validate(Account account, String user, boolean update) {
        if (account == null) {
            throw new InvalidRequestException("Account is required");
        }
        required(user, "User");
        bounded(user, "User", 30);
        required(account.getTitle(), "Title");
        bounded(account.getTitle(), "Title", 100);
        if (account.getAccountType() == null || account.getSubAccounts() == null) {
            throw new InvalidRequestException("Account type and subAccounts are required");
        }
        if (account.getSubAccounts().size() > 100) {
            throw new InvalidRequestException("At most 100 subAccounts are allowed");
        }
        if (update) {
            required(account.getId(), "Account id");
            bounded(account.getId(), "Account id", 20);
        }
        Set<String> childIds = new HashSet<>();
        for (SubAccount child : account.getSubAccounts()) {
            if (child == null) {
                throw new InvalidRequestException("SubAccount cannot be null");
            }
            required(child.getDescription(), "SubAccount description");
            bounded(child.getDescription(), "SubAccount description", 100);
            bounded(child.getId(), "SubAccount id", 20);
            if (update && child.getId() != null && (child.getId().isBlank() || !childIds.add(child.getId()))) {
                throw new InvalidRequestException("SubAccount ids must be nonblank and unique");
            }
        }
    }

    /** Validates public pagination bounds before database access. */
    public static void validatePage(int limit, int offset) {
        if (limit < 1 || limit > 100 || offset < 0 || offset > 100000) {
            throw new InvalidRequestException("limit must be 1..100 and offset 0..100000");
        }
    }

    private static void bounded(String value, String field, int maximum) {
        if (value != null && value.codePointCount(0, value.length()) > maximum) {
            throw new InvalidRequestException(field + " exceeds " + maximum + " characters");
        }
    }

    private static void required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " is required");
        }
    }
}
