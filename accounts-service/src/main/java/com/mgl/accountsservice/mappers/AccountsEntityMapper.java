package com.mgl.accountsservice.mappers;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.AccountType;
import org.springframework.stereotype.Component;

/**
 * Simple class for transforming {@link com.mgl.accountsservice.models.Account} into a
 * {@link com.mgl.accountsservice.models.SubAccount} and viceversa.
 */
@Component
public class AccountsEntityMapper {

    /**
     * Converts a {@link Account} into a {@link AccountEntity}.
     *
     * @param account .
     *
     * @return .
     */
    public AccountEntity fromModel(Account account) {
        return AccountEntity.builder()
            .id(account.getId())
            .name(account.getTitle())
            .accountType(account.getAccountType().toString())
            .createdBy(account.getCreatedBy())
            .createdAt(account.getCreatedAt())
            .lastUpdatedAt(account.getLastUpdatedAt())
            .build();
    }

    /**
     * Converts a {@link AccountEntity} into a {@link Account}.
     *
     * @param accountEntity .
     * @return .
     */
    public Account fromEntity(AccountEntity accountEntity) {
        AccountType accountType = AccountType.valueOf(accountEntity.getAccountType());
        return Account.builder()
            .id(accountEntity.getId())
            .title(accountEntity.getName())
            .accountType(accountType)
            .createdBy(accountEntity.getCreatedBy())
            .createdAt(accountEntity.getCreatedAt())
            .lastUpdatedAt(accountEntity.getLastUpdatedAt())
            .build();
    }

}
