package com.mgl.accountsservice.dao.impl;

import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * .
 */
@Log4j2
@Component
public class MyBatisSubAccountsDao implements SubAccountsDao {

    @Transactional
    @Override
    public void insertSubAccount(SubAccountEntity subAccountEntity) throws DatabaseException {

    }
}
