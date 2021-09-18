package com.mgl.accountsservice.dao.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.dao.mappers.SubAccountsMapper;
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
public class MyBatisSubAccountsDaoTests {

    @Mock
    private SubAccountsMapper subAccountsMapper;
    @Mock
    private RandomIdGenerator randomIdGenerator;

    private MyBatisSubAccountsDao myBatisSubAccountsDao;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        myBatisSubAccountsDao = new MyBatisSubAccountsDao(subAccountsMapper, randomIdGenerator);
    }

    @Test
    public void insertSubAccount_should_insert() throws DatabaseException {
        String generatedId = EnhancedRandom.random(String.class);
        when(randomIdGenerator.generateRandomId(any())).thenReturn(generatedId);

        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        String subAccountId = myBatisSubAccountsDao.insertSubAccount(subAccountEntity);

        subAccountEntity.setId(generatedId);
        verify(subAccountsMapper).insertSubAccount(subAccountEntity);
        assertThat(subAccountId).isEqualTo(generatedId);
    }

    @Test
    public void getSubAccounts_should_returnSubAccounts() throws Exception {
        String accountId = EnhancedRandom.random(String.class);

        List<SubAccountEntity> expectedSubAccounts =
            EnhancedRandom.randomListOf(5, SubAccountEntity.class);
        when(subAccountsMapper.getSubAccounts(accountId)).thenReturn(expectedSubAccounts);

        List<SubAccountEntity> subAcounts = myBatisSubAccountsDao.getSubAccounts(accountId);

        assertThat(subAcounts).isEqualTo(expectedSubAccounts);
    }

    @Test
    public void getSubAccounts_should_bubbleUpExceptions() throws Exception {
        when(subAccountsMapper.getSubAccounts(any())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> myBatisSubAccountsDao.getSubAccounts("Id"))
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void getSubAccount_should_returnSubAccount() throws Exception {
        String subAccountId = "SubAccountId";
        SubAccountEntity expectedSubAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        when(subAccountsMapper.getSubAccount(subAccountId)).thenReturn(expectedSubAccountEntity);

        SubAccountEntity subAccountEntity = myBatisSubAccountsDao.getSubAccount(subAccountId);

        assertThat(subAccountEntity).isEqualTo(expectedSubAccountEntity);
    }

    @Test
    public void getSubAccount_should_bubbleUpException() throws Exception {
        when(subAccountsMapper.getSubAccount(anyString())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> myBatisSubAccountsDao.getSubAccount("Id"))
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void deleteSubAccount_should_deleteSubAccount() throws Exception {
        String subAccountId = "SomeId";

        myBatisSubAccountsDao.deleteSubAccount(subAccountId);

        verify(subAccountsMapper).deleteSubAccount(subAccountId);
    }

    @Test
    public void deleteSubAccount_should_bubbleUpException() throws Exception {
        doThrow(RuntimeException.class).when(subAccountsMapper).deleteSubAccount(anyString());

        assertThatThrownBy(() -> myBatisSubAccountsDao.deleteSubAccount("someSome"))
            .isInstanceOfAny(DatabaseException.class);
    }

    @Test
    public void putSubAccount_should_put() throws Exception {
        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);

        myBatisSubAccountsDao.putSubAccount(subAccountEntity);

        verify(subAccountsMapper).putSubAccount(subAccountEntity);
    }

    @Test
    public void putSubAccount_should_bubbleUpException() throws Exception {
        doThrow(RuntimeException.class).when(subAccountsMapper).putSubAccount(any());

        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);
        assertThatThrownBy(() -> myBatisSubAccountsDao.putSubAccount(subAccountEntity))
            .isInstanceOfAny(DatabaseException.class);
    }

}
