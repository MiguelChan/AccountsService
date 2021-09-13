package com.mgl.accountsservice.components;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.Optional;
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
public class GetAccountByIdComponentTests {

    private static final String TEST_ACCOUNT_ID = "SomeId";

    @Mock
    private AccountsDao accountsDao;
    @Mock
    private SubAccountsDao subAccountsDao;
    @Mock
    private AccountsEntityMapper accountsEntityMapper;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;

    private GetAccountByIdComponent component;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        component = new GetAccountByIdComponent(
            accountsDao,
            accountsEntityMapper,
            subAccountsDao,
            subAccountsEntityMapper
        );
    }

    @Test
    public void getAccount_should_getTheAccount() {
        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenReturn(accountEntity);

        Account partialAccount = EnhancedRandom.random(Account.class);
        when(accountsEntityMapper.fromEntity(accountEntity)).thenReturn(partialAccount);

        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);
        when(subAccountsDao.getSubAccounts(TEST_ACCOUNT_ID))
            .thenReturn(Lists.newArrayList(subAccountEntity));
        SubAccount expectedSubAccount = EnhancedRandom.random(SubAccount.class);
        when(subAccountsEntityMapper.fromEntity(subAccountEntity)).thenReturn(expectedSubAccount);

        Account expectedAccount = partialAccount.toBuilder()
            .subAccounts(Lists.newArrayList(expectedSubAccount))
            .build();

        Optional<Account> foundAccountOpt = component.getAccount(TEST_ACCOUNT_ID);

        assertThat(foundAccountOpt).isNotNull();
        assertThat(foundAccountOpt.isEmpty()).isFalse();
        assertThat(foundAccountOpt.get()).isEqualTo(expectedAccount);
    }

    @Test
    public void getAccount_should_returnEmpty_when_AccountDoesNotExist() {
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenReturn(null);

        Optional<Account> foundAccountOpt = component.getAccount(TEST_ACCOUNT_ID);

        assertThat(foundAccountOpt.isEmpty()).isTrue();
    }

    @Test
    public void getAccount_should_returnEmpty_when_databaseExceptionOccurs() {
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenThrow(DatabaseException.class);

        Optional<Account> foundAccountOpt = component.getAccount(TEST_ACCOUNT_ID);

        assertThat(foundAccountOpt.isEmpty()).isTrue();
    }

}
