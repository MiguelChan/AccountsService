package com.mgl.accountsservice.dao;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import java.util.List;

/**
 * Defines the interface for accessing the DataStorage for Accounts.
 */
public interface AccountsDao {

    /**
     * Inserts a {@link AccountEntity} into the DataStorage.
     *
     * @param accountEntity The entity to insert.
     *
     * @return The new id of the Account.
     */
    String insertAccount(AccountEntity accountEntity) throws DatabaseException;

    /**
     * Gets all the {@link AccountEntity}s from the Database.
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    List<AccountEntity> getAccounts() throws DatabaseException;

    /**
     * Gets an {@link AccountEntity} based off its Id.
     *
     * @param accountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    AccountEntity getAccount(String accountId) throws DatabaseException;

    /**
     * Deletes a {@link AccountEntity} based off its id.
     *
     * @param accountId .
     *
     * @throws DatabaseException .
     */
    void deleteAccount(String accountId) throws DatabaseException;

}
