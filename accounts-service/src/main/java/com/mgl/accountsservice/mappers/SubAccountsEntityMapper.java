package com.mgl.accountsservice.mappers;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.models.SubAccount;
import org.springframework.stereotype.Component;

/**
 * Class that converts a {@link com.mgl.accountsservice.models.SubAccount} into a
 * {@link SubAccountEntity}.
 */
@Component
public class SubAccountsEntityMapper {

    /**
     * Converts a {@link SubAccount} into a {@link SubAccountEntity}.
     *
     * @param subAccount .
     * @return .
     */
    public SubAccountEntity fromModel(SubAccount subAccount) {
        return SubAccountEntity.builder()
            .id(subAccount.getId())
            .description(subAccount.getDescription())
            .createdAt(subAccount.getCreatedAt())
            .lastUpdatedAt(subAccount.getLastUpdatedAt())
            .createdBy(subAccount.getCreatedBy())
            .build();
    }

    /**
     * Converts a {@link SubAccountEntity} into a {@link SubAccount}.
     *
     * @param subAccountEntity .
     * @return .
     */
    public SubAccount fromEntity(SubAccountEntity subAccountEntity) {
        return SubAccount.builder()
            .id(subAccountEntity.getId())
            .description(subAccountEntity.getDescription())
            .createdAt(subAccountEntity.getCreatedAt())
            .createdBy(subAccountEntity.getCreatedBy())
            .lastUpdatedAt(subAccountEntity.getLastUpdatedAt())
            .build();
    }

}
