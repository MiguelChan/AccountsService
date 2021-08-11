package com.mgl.accountsservice.integration.utils;

import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;

/**
 * .
 */
public class DataCurator {

    private static final int ID_LENGTH = 20;
    private static final int NAME_LENGTH = 10;
    private static final int CREATED_BY_LENGTH = 30;
    private static final int DESCRIPTION_LENGTH = 100;

    /**
     * .
     *
     * @param account .
     *
     * @return .
     */
    public Account curateAccountData(Account account) {
        return account.toBuilder()
            .id(account.getId().substring(0, Math.min(account.getId().length(), ID_LENGTH)))
            .title(account.getTitle().substring(0, Math.min(account.getTitle().length(), NAME_LENGTH)))
            .createdBy(account.getCreatedBy().substring(0, Math.min(account.getCreatedBy().length(), CREATED_BY_LENGTH)))
            .build();
    }

    /**
     * .
     *
     * @param subAccount .
     *
     * @return .
     */
    public SubAccount curateSubAccount(SubAccount subAccount) {
        String id = subAccount.getId();
        String description = subAccount.getDescription();
        String createdBy = subAccount.getCreatedBy();

        return subAccount.toBuilder()
            .id(id.substring(0, Math.min(id.length(), ID_LENGTH)))
            .description(description.substring(0, Math.min(description.length(), DESCRIPTION_LENGTH)))
            .createdBy(createdBy.substring(0, Math.min(createdBy.length(), CREATED_BY_LENGTH)))
            .build();
    }
}
