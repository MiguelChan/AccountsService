package com.mgl.accountsservice.dao.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

}
