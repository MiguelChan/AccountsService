package com.mgl.accountsservice.components;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
public class CreateAccountComponentTests {

    @Mock
    private SubAccountsDao subAccountsDao;
    @Mock
    private AccountsDao accountsDao;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;
    @Mock
    private AccountsEntityMapper accountsEntityMapper;

    private CreateAccountComponent createAccountComponent;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        createAccountComponent = new CreateAccountComponent(
            accountsDao,
            subAccountsDao,
            accountsEntityMapper,
            subAccountsEntityMapper
        );
    }

    @Test
    public void createAccount_should_createAnAccount() {
        String testUserId = "someRandomUser";
        String expectedAccountId = EnhancedRandom.random(String.class);

        Account account = EnhancedRandom.random(Account.class, "subAccounts");
        SubAccount subAccount = EnhancedRandom.random(SubAccount.class);

        account.setSubAccounts(Lists.newArrayList(subAccount));

        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        accountEntity.setCreatedBy(testUserId);
        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        when(accountsDao.insertAccount(accountEntity)).thenReturn(expectedAccountId);

        when(accountsEntityMapper.fromModel(account)).thenReturn(accountEntity);
        when(subAccountsEntityMapper.fromModel(subAccount)).thenReturn(subAccountEntity);

        createAccountComponent.createAccount(account, testUserId);

        verify(accountsDao).insertAccount(accountEntity);

        subAccountEntity.setAccountId(expectedAccountId);
        verify(subAccountsDao).insertSubAccount(subAccountEntity);
    }

    @Test
    public void createAccount_should_bubbleUpException_when_somethingFails() {
        String testUserId = "someRandomUser";

        Account account = EnhancedRandom.random(Account.class, "subAccounts");
        SubAccount subAccount = EnhancedRandom.random(SubAccount.class);

        account.setSubAccounts(Lists.newArrayList(subAccount));

        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        accountEntity.setCreatedBy(testUserId);
        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        when(accountsEntityMapper.fromModel(account)).thenReturn(accountEntity);
        when(subAccountsEntityMapper.fromModel(subAccount)).thenReturn(subAccountEntity);
        doThrow(DatabaseException.class).when(accountsDao).insertAccount(accountEntity);

        assertThrows(DatabaseException.class, () -> createAccountComponent.createAccount(account, testUserId));
    }

}
