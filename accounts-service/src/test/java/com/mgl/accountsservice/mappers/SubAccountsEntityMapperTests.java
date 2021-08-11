package com.mgl.accountsservice.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * .
 */
public class SubAccountsEntityMapperTests {

    private SubAccountsEntityMapper mapper;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        mapper = new SubAccountsEntityMapper();
    }

    @Test
    public void fromModel_should_createAnEntity() {
        SubAccount subAccount = EnhancedRandom.random(SubAccount.class);

        SubAccountEntity entity = mapper.fromModel(subAccount);

        assertThat(entity.getId()).isEqualTo(subAccount.getId());
        assertThat(entity.getDescription()).isEqualTo(subAccount.getDescription());
        assertThat(entity.getCreatedAt()).isEqualTo(subAccount.getCreatedAt());
        assertThat(entity.getLastUpdatedAt()).isEqualTo(subAccount.getLastUpdatedAt());
        assertThat(entity.getCreatedBy()).isEqualTo(subAccount.getCreatedBy());
    }

    @Test
    public void fromEntity_should_createaModel() {
        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        SubAccount subAccount = mapper.fromEntity(subAccountEntity);

        assertThat(subAccount.getId()).isEqualTo(subAccountEntity.getId());
        assertThat(subAccount.getDescription()).isEqualTo(subAccountEntity.getDescription());
        assertThat(subAccount.getCreatedAt()).isEqualTo(subAccountEntity.getCreatedAt());
        assertThat(subAccount.getLastUpdatedAt()).isEqualTo(subAccountEntity.getLastUpdatedAt());
        assertThat(subAccount.getCreatedBy()).isEqualTo(subAccountEntity.getCreatedBy());
    }

}
