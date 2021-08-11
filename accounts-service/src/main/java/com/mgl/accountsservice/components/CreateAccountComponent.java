package com.mgl.accountsservice.components;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component for Creating Accounts.
 */
@Log4j2
@Component
public class CreateAccountComponent {

    private final AccountsDao accountsDao;
    private final SubAccountsDao subAccountsDao;
    private final AccountsEntityMapper accountsEntityMapper;
    private final SubAccountsEntityMapper subAccountsEntityMapper;

    /**
     * Constructor.
     *
     * @param accountsDao .
     *
     * @param subAccountsDao .
     *
     * @param accountsEntityMapper .
     *
     * @param subAccountsEntityMapper .
     *
     */
    @Autowired
    public CreateAccountComponent(AccountsDao accountsDao,
                                  SubAccountsDao subAccountsDao,
                                  AccountsEntityMapper accountsEntityMapper,
                                  SubAccountsEntityMapper subAccountsEntityMapper) {
        this.accountsDao = accountsDao;
        this.subAccountsDao = subAccountsDao;
        this.accountsEntityMapper = accountsEntityMapper;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }

    /**
     * Inserts the provided {@link Account} into the Data layer.
     *
     * @param account .
     *
     * @param requestingUser .
     */
    @Transactional
    public void createAccount(Account account, String requestingUser) {
        log.info("Attempting to insert new account: {}", account);

        AccountEntity accountEntity = accountsEntityMapper.fromModel(account);
        accountEntity.setCreatedBy(requestingUser);
        List<SubAccountEntity> subAccountEntities = account.getSubAccounts()
            .stream()
            .map(subAccountsEntityMapper::fromModel)
            .collect(Collectors.toList());

        try {
            accountsDao.insertAccount(accountEntity);
            for (SubAccountEntity currentEntity : subAccountEntities) {
                subAccountsDao.insertSubAccount(currentEntity);
            }
        } catch (DatabaseException e) {
            log.error("An error occurred when trying to insert the Account.", e);
            throw e;
        }
    }

}
