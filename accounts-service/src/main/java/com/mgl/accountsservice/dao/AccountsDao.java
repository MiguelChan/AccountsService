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
     * Gets the first 50 {@link AccountEntity}s from the Database.
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    default List<AccountEntity> getAccounts() throws DatabaseException {
        return getAccounts(50, 0);
    }

    /** Retrieves a bounded, ordered page. */
    List<AccountEntity> getAccounts(int limit, int offset) throws DatabaseException;

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

    /** Locks a parent row while validating and updating its children. */
    AccountEntity getAccountForUpdate(String accountId) throws DatabaseException;

    /**
     * Deletes a {@link AccountEntity} based off its id.
     *
     * @param accountId .
     *
     * @throws DatabaseException .
     */
    void deleteAccount(String accountId) throws DatabaseException;

    /**
     * Updates (PUT) a {@link AccountEntity}.
     *
     * @param accountEntity .
     *
     * @throws DatabaseException .
     */
    void putAccount(AccountEntity accountEntity) throws DatabaseException;

}
