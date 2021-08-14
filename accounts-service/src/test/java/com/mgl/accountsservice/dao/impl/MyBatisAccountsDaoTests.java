package com.mgl.accountsservice.dao.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.mappers.AccountsMapper;
import com.mgl.accountsservice.utils.RandomIdGenerator;
import io.github.benas.randombeans.api.EnhancedRandom;
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

}
