package com.mgl.accountsservice.components;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component to be used whenever we want to "Put" (Edit) an existing Account.
 */
@Component
public class PutAccountComponent {

    private final AccountsDao accountsDao;
    private final AccountsEntityMapper accountsEntityMapper;
    private final SubAccountsDao subAccountsDao;
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
    @Autowired
    public PutAccountComponent(AccountsDao accountsDao,
                               AccountsEntityMapper accountsEntityMapper,
                               SubAccountsDao subAccountsDao,
                               SubAccountsEntityMapper subAccountsEntityMapper) {
        this.accountsDao = accountsDao;
        this.subAccountsDao = subAccountsDao;
        this.accountsEntityMapper = accountsEntityMapper;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }

    /**
     * Puts an {@link Account} and its associated {@link SubAccount}s into the Storage Layer.
     *
     * @param newAccount .
     *
     * @param updatingUser .
     *
     * @return .
     */
    @Transactional
    public Account putAccount(Account newAccount, String updatingUser) {
        validateAccount(newAccount);

        List<SubAccountEntity> existingSubAccounts =
            getSubAccounts(newAccount, (subAccount -> subAccount.getId() != null));
        List<SubAccountEntity> newSubAccounts =
            getSubAccounts(newAccount, (subAccount -> subAccount.getId() == null));

        existingSubAccounts.forEach(subAccountsDao::putSubAccount);
        newSubAccounts.forEach(subAccountsDao::insertSubAccount);

        AccountEntity accountEntity = accountsEntityMapper.fromModel(newAccount);
        accountsDao.putAccount(accountEntity);

        String accountId = newAccount.getId();
        // We finally fetch the newly created Account and its subaccounts.
        AccountEntity updatedAccount = accountsDao.getAccount(accountId);
        List<SubAccountEntity> updatedSubAccounts = subAccountsDao.getSubAccounts(accountId);

        List<SubAccount> subAccounts = updatedSubAccounts
            .stream()
            .map(subAccountsEntityMapper::fromEntity)
            .collect(Collectors.toList());

        Account account = accountsEntityMapper.fromEntity(updatedAccount);

        return account.toBuilder()
            .subAccounts(subAccounts)
            .build();
    }

    private List<SubAccountEntity> getSubAccounts(Account account, Predicate<SubAccount> predicate) {
        String accountId = account.getId();

        List<SubAccountEntity> subAccounts = account.getSubAccounts()
            .stream()
            .filter(predicate)
            .map(subAccountsEntityMapper::fromModel)
            .collect(Collectors.toList());
        subAccounts.forEach(currentSubAccount -> currentSubAccount.setAccountId(accountId));

        return subAccounts;
    }

    private void validateAccount(Account anAccount) {
        Objects.requireNonNull(anAccount, "Account can't be null");
        Objects.requireNonNull(anAccount.getId(), "Account Id is required");
    }

}
