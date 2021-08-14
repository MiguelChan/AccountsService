package com.mgl.accountsservice.dao;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;

/**
 * Defines the interface for accessing the DataStorage for
 * {@link com.mgl.accountsservice.models.SubAccount} objects.
 */
public interface SubAccountsDao {

    /**
     * Inserts the provided {@link SubAccountEntity} into the Database.
     *
     * @param subAccountEntity .
     *
     * @throws DatabaseException .
     */
    String insertSubAccount(SubAccountEntity subAccountEntity) throws DatabaseException;

}
