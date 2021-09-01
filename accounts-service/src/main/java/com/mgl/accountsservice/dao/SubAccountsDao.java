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

    /**
     * Deletes the provided {@link SubAccountEntity} given the provided Id.
     *
     * @param subAccountId .
     *
     * @throws DatabaseException .
     */
    void deleteSubAccount(String subAccountId) throws DatabaseException;

    /**
     * Retrieves a {@link SubAccountEntity} using the provided Id.
     *
     * @param subAccountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    SubAccountEntity getSubAccount(String subAccountId) throws DatabaseException;

}
