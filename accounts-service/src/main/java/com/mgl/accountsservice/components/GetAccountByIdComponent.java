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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component for retrieving a single Account by Its ID.
 */
@Log4j2
@Component
public class GetAccountByIdComponent {

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
    public GetAccountByIdComponent(AccountsDao accountsDao,
                                   AccountsEntityMapper accountsEntityMapper,
                                   SubAccountsDao subAccountsDao,
                                   SubAccountsEntityMapper subAccountsEntityMapper) {
        this.accountsDao = accountsDao;
        this.accountsEntityMapper = accountsEntityMapper;
        this.subAccountsDao = subAccountsDao;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }

    /**
     * Gets an {@link Account} by its Id.
     *
     * @param accountId .
     *
     * @return .
     */
    public Optional<Account> getAccount(String accountId) {
        try {
            AccountEntity accountEntity = accountsDao.getAccount(accountId);
            if (accountEntity == null) {
                return Optional.empty();
            }

            List<SubAccountEntity> subAccountEntities = subAccountsDao.getSubAccounts(accountId);
            List<SubAccount> subAccounts = subAccountEntities.stream()
                .map(subAccountsEntityMapper::fromEntity)
                .collect(Collectors.toList());

            Account foundAccount = accountsEntityMapper.fromEntity(accountEntity);

            Account finalAccount = foundAccount.toBuilder()
                .subAccounts(subAccounts)
                .build();

            return Optional.of(finalAccount);
        } catch (DatabaseException databaseException) {
            log.error("An error occurred when trying to fetch AccountId",
                databaseException
            );
            return Optional.empty();
        }
    }

}
