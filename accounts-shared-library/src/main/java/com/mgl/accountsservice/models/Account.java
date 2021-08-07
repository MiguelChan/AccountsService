package com.mgl.accountsservice.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Defines an Account that can be used.
 * An account has a Title, an {@link AccountType} and a List of {@link SubAccount}.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Account extends Auditable {
    private String title;
    private AccountType accountType;
    private List<SubAccount> subAccounts;
}
