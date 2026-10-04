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
        required(account.getTitle(), "Title");
        if (account.getAccountType() == null || account.getSubAccounts() == null) {
            throw new InvalidRequestException("Account type and subAccounts are required");
        }
        if (update) {
            required(account.getId(), "Account id");
        }
        Set<String> childIds = new HashSet<>();
        for (SubAccount child : account.getSubAccounts()) {
            if (child == null) {
                throw new InvalidRequestException("SubAccount cannot be null");
            }
            required(child.getDescription(), "SubAccount description");
            if (update && child.getId() != null && (child.getId().isBlank() || !childIds.add(child.getId()))) {
                throw new InvalidRequestException("SubAccount ids must be nonblank and unique");
            }
        }
    }

    private static void required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " is required");
        }
    }
}
