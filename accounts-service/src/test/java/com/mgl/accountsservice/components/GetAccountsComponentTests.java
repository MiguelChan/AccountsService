package com.mgl.accountsservice.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class GetAccountsComponentTests {

    @Mock
    private AccountsDao accountsDao;
    @Mock
    private SubAccountsDao subAccountsDao;

    @Mock
    private AccountsEntityMapper accountsEntityMapper;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;

    private GetAccountsComponent component;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        component = new GetAccountsComponent(
            accountsDao,
            accountsEntityMapper,
            subAccountsDao,
            subAccountsEntityMapper
        );
    }

    @Test
    public void getAccounts_should_returnAllAccounts() {
        // 1.- Account setup.
        String accountId = EnhancedRandom.random(String.class);
        AccountEntity accountEntity =
            EnhancedRandom.random(AccountEntity.class, "id");
        accountEntity.setId(accountId);

        Account randomAccount = EnhancedRandom.random(Account.class, "subAccounts");
        when(accountsEntityMapper.fromEntity(accountEntity)).thenReturn(randomAccount);
        when(accountsDao.getAccounts()).thenReturn(Lists.newArrayList(accountEntity));

        // 2.- SubAccount setup
        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);
        SubAccount expectedSubAccount = EnhancedRandom.random(SubAccount.class);

        when(subAccountsEntityMapper.fromEntity(subAccountEntity)).thenReturn(expectedSubAccount);
        when(subAccountsDao.getSubAccounts(accountId))
            .thenReturn(Lists.newArrayList(subAccountEntity));

        // 3.- Final object setup
        Account expectedAccount = randomAccount.toBuilder()
            .subAccounts(Lists.newArrayList(expectedSubAccount))
            .build();

        // Act
        List<Account> accounts = component.getAccounts();

        // Assert
        assertThat(accounts.size()).isEqualTo(1);

        Account foundAccount = accounts.get(0);
        assertThat(foundAccount).isEqualTo(expectedAccount);

    }

    @Test
    public void getAccounts_should_bubbleUpException() {
        when(accountsDao.getAccounts()).thenThrow(DatabaseException.class);

        assertThatThrownBy(() -> component.getAccounts()).isInstanceOfAny(DatabaseException.class);
    }

}
