package com.mgl.accountsservice.models;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Defines an Account that can be used.
 * An account has a Title, an {@link AccountType} and a List of {@link SubAccount}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Account extends Auditable {
    private String id;
    private String title;
    private AccountType accountType;
    private List<SubAccount> subAccounts;

    /**
     * .
     *
     * @param createdBy .
     *
     * @param createdAt .
     *
     * @param lastUpdatedAt .
     *
     * @param id .
     *
     * @param title .
     *
     * @param accountType .
     *
     * @param subAccounts .
     */
    @Builder(toBuilder = true)
    public Account(String createdBy,
                   LocalDateTime createdAt,
                   LocalDateTime lastUpdatedAt,
                   String id,
                   String title,
                   AccountType accountType,
                   List<SubAccount> subAccounts) {
        super(createdBy, createdAt, lastUpdatedAt);
        this.id = id;
        this.title = title;
        this.accountType = accountType;
        this.subAccounts = subAccounts;
    }
}
