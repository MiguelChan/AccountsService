package com.mgl.accountsservice.dao.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.mappers.AccountsMapper;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.utils.RandomIdGenerator;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class MyBatisAccountsDaoTests {

    @Mock
    private AccountsMapper accountsMapper;
    @Mock
    private RandomIdGenerator randomIdGenerator;

    private MyBatisAccountsDao accountsDao;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        accountsDao = new MyBatisAccountsDao(accountsMapper, randomIdGenerator);
    }

    @Test
    public void insertAccount_should_insertTheAccount() throws Exception {
        String randomId = EnhancedRandom.random(String.class);
        when(randomIdGenerator.generateRandomId(any())).thenReturn(randomId);

        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class, "id");

        String newId = accountsDao.insertAccount(accountEntity);

        accountEntity.setId(randomId);

        verify(accountsMapper).insertAccount(accountEntity);
        assertThat(newId).isEqualTo(randomId);
    }

    @Test
    public void getAccounts_should_getTheAccounts() throws Exception {
        List<AccountEntity> expectedAccounts =
            EnhancedRandom.randomListOf(5, AccountEntity.class);

        when(accountsMapper.getAccounts()).thenReturn(expectedAccounts);

        List<AccountEntity> foundAccounts = accountsDao.getAccounts();

        assertThat(foundAccounts).isEqualTo(expectedAccounts);
    }

    @Test
    public void getAccounts_should_bubbleUpExceptions() throws Exception {
        when(accountsMapper.getAccounts()).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> accountsDao.getAccounts())
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void getAccount_should_getAccount() throws Exception {
        String testAccountId = "SomeId";

        AccountEntity expectedAccountEntity = EnhancedRandom.random(AccountEntity.class);

        when(accountsMapper.getAccount(testAccountId)).thenReturn(expectedAccountEntity);

        AccountEntity foundAccount = accountsDao.getAccount(testAccountId);

        assertThat(foundAccount).isEqualTo(expectedAccountEntity);
    }

    @Test
    public void getAccount_should_bubbleUpException() throws Exception {
        when(accountsMapper.getAccount(any())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> accountsDao.getAccount("SomeSome"))
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void deleteAccount_should_delete() throws Exception {
        String testAccountId = "SomeId";

        accountsDao.deleteAccount(testAccountId);

        verify(accountsMapper).deleteAccount(testAccountId);
    }

    @Test
    public void deleteAccount_should_bubbleUpExceptions() throws Exception {
        doThrow(RuntimeException.class).when(accountsMapper).deleteAccount(any());

        assertThatThrownBy(() -> accountsDao.deleteAccount("someSome"))
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void putAccount_should_put() throws Exception {
        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);

        accountsDao.putAccount(accountEntity);

        verify(accountsMapper).putAccount(accountEntity);
    }

    @Test
    public void putAccount_should_bubbleUpExceptions() throws Exception {
        doThrow(RuntimeException.class).when(accountsMapper).putAccount(any());
        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);

        assertThatThrownBy(() -> accountsDao.putAccount(accountEntity)).isInstanceOfAny(DatabaseException.class);
    }

}
