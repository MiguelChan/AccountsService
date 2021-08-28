package com.mgl.accountsservice.dao;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import java.util.List;

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

    /**
     * Gets all the {@link SubAccountEntity}s that matches the provide accountId.
     *
     * @param accountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    List<SubAccountEntity> getSubAccounts(String accountId) throws DatabaseException;

}
