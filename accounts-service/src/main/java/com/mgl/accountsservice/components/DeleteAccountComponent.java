package com.mgl.accountsservice.components;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Component for deleting Accounts.
 */
@Component
public class DeleteAccountComponent {

    private final AccountsDao accountsDao;
    private final SubAccountsDao subAccountsDao;
    private final AccountsEntityMapper accountsEntityMapper;
    private final SubAccountsEntityMapper subAccountsEntityMapper;

    /**
     * .
     *
     * @param accountsDao .
     *
     * @param accountsEntityMapper .
     *
     * @param subAccountsDao .
     *
     * @param subAccountsEntityMapper .
     */
    public DeleteAccountComponent(AccountsDao accountsDao,
                                  AccountsEntityMapper accountsEntityMapper,
                                  SubAccountsDao subAccountsDao,
                                  SubAccountsEntityMapper subAccountsEntityMapper) {
        this.accountsDao = accountsDao;
        this.accountsEntityMapper = accountsEntityMapper;
        this.subAccountsDao = subAccountsDao;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }

    /**
     * Deletes an Account. Returns an Empty Optional if an error ocurred.
     *
     * @param accountId .
     *
     * @return .
     */
    public Optional<Account> deleteAccount(String accountId) {
        Optional<Account> foundAccount = getAccount(accountId);

        if (foundAccount.isEmpty()) {
            return foundAccount;
        }

        accountsDao.deleteAccount(accountId);

        return foundAccount;
    }

    private Optional<Account> getAccount(String accountId) {
        try {
            AccountEntity accountEntity = accountsDao.getAccount(accountId);
            if (accountEntity == null) {
                return Optional.empty();
            }

            List<SubAccountEntity> subAccountEntities = subAccountsDao.getSubAccounts(accountId);

            List<SubAccount> subAccounts = subAccountEntities.stream()
                .map(subAccountsEntityMapper::fromEntity)
                .collect(Collectors.toList());

            Account account = accountsEntityMapper.fromEntity(accountEntity);

            Account finalAccount = account.toBuilder()
                .subAccounts(subAccounts)
                .build();

            return Optional.of(finalAccount);
        } catch (DatabaseException e) {
            return Optional.empty();
        }
    }

}
