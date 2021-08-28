package com.mgl.accountsservice.dao.impl;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.mappers.AccountsMapper;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.utils.RandomIdGenerator;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The MyBatis implemetantion of {@link AccountsDao}.
 */
@Log4j2
@Component
public class MyBatisAccountsDao implements AccountsDao {

    private static final String ACCOUNT_PREFIX = "acct";

    private final AccountsMapper accountsMapper;
    private final RandomIdGenerator randomIdGenerator;

    @Autowired
    public MyBatisAccountsDao(AccountsMapper accountsMapper, RandomIdGenerator randomIdGenerator) {
        this.accountsMapper = accountsMapper;
        this.randomIdGenerator = randomIdGenerator;
    }

    @Transactional
    @Override
    public String insertAccount(AccountEntity accountEntity) throws DatabaseException {
        String accountId = randomIdGenerator.generateRandomId(ACCOUNT_PREFIX);
        accountEntity.setId(accountId);

        log.info("Attempting to insert the AccountEntity: {}", accountEntity);
        log.info("Generated Id is: {}", accountId);

        accountsMapper.insertAccount(accountEntity);

        return accountId;
    }

    @Override
    public List<AccountEntity> getAccounts() throws DatabaseException {
        log.info("Attempting to retrieve all Accounts");
        try {
            return accountsMapper.getAccounts();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
