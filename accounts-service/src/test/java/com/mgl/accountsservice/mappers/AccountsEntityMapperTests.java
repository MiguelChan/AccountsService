package com.mgl.accountsservice.mappers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.AccountType;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * .
 */
public class AccountsEntityMapperTests {

    private AccountsEntityMapper accountsEntityMapper;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        accountsEntityMapper = new AccountsEntityMapper();
    }

    @Test
    public void fromModel_should_createAnEntity() {
        Account account = EnhancedRandom.random(Account.class);

        AccountEntity accountEntity = accountsEntityMapper.fromModel(account);

        assertThat(accountEntity.getId()).isEqualTo(account.getId());
        assertThat(accountEntity.getName()).isEqualTo(account.getTitle());
        assertThat(accountEntity.getAccountType()).isEqualTo(account.getAccountType().toString());
        assertThat(accountEntity.getCreatedAt()).isEqualTo(account.getCreatedAt());
        assertThat(accountEntity.getCreatedBy()).isEqualTo(account.getCreatedBy());
        assertThat(accountEntity.getLastUpdatedAt()).isEqualTo(account.getLastUpdatedAt());
    }

    @Test
    public void fromEntity_should_createaModel() {
        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        accountEntity.setAccountType(AccountType.Entry.toString());

        Account account = accountsEntityMapper.fromEntity(accountEntity);

        assertThat(account.getId()).isEqualTo(accountEntity.getId());
        assertThat(account.getTitle()).isEqualTo(accountEntity.getName());
        assertThat(account.getAccountType().toString()).isEqualTo(accountEntity.getAccountType());
        assertThat(account.getCreatedAt()).isEqualTo(accountEntity.getCreatedAt());
        assertThat(account.getCreatedBy()).isEqualTo(accountEntity.getCreatedBy());
        assertThat(account.getLastUpdatedAt()).isEqualTo(accountEntity.getLastUpdatedAt());
    }

}
