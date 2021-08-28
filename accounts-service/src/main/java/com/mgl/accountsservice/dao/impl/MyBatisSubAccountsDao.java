package com.mgl.accountsservice.dao.impl;

import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.dao.mappers.SubAccountsMapper;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.utils.RandomIdGenerator;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * .
 */
@Log4j2
@Component
public class MyBatisSubAccountsDao implements SubAccountsDao {

    private static final String SUBACCOUNT_PREFIX = "sbacct";

    private final SubAccountsMapper subAccountsMapper;
    private final RandomIdGenerator randomIdGenerator;

    @Autowired
    public MyBatisSubAccountsDao(SubAccountsMapper subAccountsMapper,
                                 RandomIdGenerator randomIdGenerator) {
        this.subAccountsMapper = subAccountsMapper;
        this.randomIdGenerator = randomIdGenerator;
    }

    @Transactional
    @Override
    public String insertSubAccount(SubAccountEntity subAccountEntity) throws DatabaseException {
        String subAccountId = randomIdGenerator.generateRandomId(SUBACCOUNT_PREFIX);
        subAccountEntity.setId(subAccountId);

        log.info("Attempting to insert the SubAccountEntity: {}", subAccountEntity);
        log.info("Generated SubAccount Id is: {}", subAccountId);

        subAccountsMapper.insertSubAccount(subAccountEntity);

        return subAccountId;
    }

    @Override
    public List<SubAccountEntity> getSubAccounts(String accountId) throws DatabaseException {
        log.info("Attempting to get SubAccounts for Account: {}", accountId);
        try {
            return subAccountsMapper.getSubAccounts(accountId);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
