package com.mgl.accountsservice.components;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component that retrieves all the {@link Account}s with their respective
 * {@link com.mgl.accountsservice.models.SubAccount}.
 */
@Component
public class GetAccountsComponent {

    private final AccountsDao accountsDao;
    private final AccountsEntityMapper accountsEntityMapper;
    private final SubAccountsDao subAccountsDao;
    private final SubAccountsEntityMapper subAccountsEntityMapper;

    /**
     * Default constructor.
     *
     * @param accountsDao .
     *
     * @param subAccountsDao .
     */
    @Autowired
    public GetAccountsComponent(AccountsDao accountsDao,
                                AccountsEntityMapper accountsEntityMapper,
                                SubAccountsDao subAccountsDao,
                                SubAccountsEntityMapper subAccountsEntityMapper) {
        this.accountsDao = accountsDao;
        this.accountsEntityMapper = accountsEntityMapper;
        this.subAccountsDao = subAccountsDao;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }

    /**
     * Retrieves all the {@link Account} with their corresponding {@link SubAccount}s from the
     * Storage Layer.
     *
     * @return A List of {@link Account}.
     */
    public List<Account> getAccounts() {
        // 1.- We fetch the Account Entities.
        List<AccountEntity> accountEntities = accountsDao.getAccounts();
        // 2.- We create a Map of Account to List<SubAccount>
        Map<AccountEntity, List<SubAccountEntity>> accountToSubAccountMap = new HashMap<>();
        accountEntities.parallelStream().forEach(currentAccount -> {
            String accountId = currentAccount.getId();
            List<SubAccountEntity> subAccounts = subAccountsDao.getSubAccounts(accountId);
            accountToSubAccountMap.put(currentAccount, subAccounts);
        });
        // 3.- Finally, we create the corresponding AppModels.
        return accountToSubAccountMap.entrySet()
            .stream()
            .map(currentEntry -> {
                List<SubAccount> subAccounts = currentEntry.getValue()
                    .stream()
                    .map(subAccountsEntityMapper::fromEntity)
                    .collect(Collectors.toList());
                Account account = accountsEntityMapper.fromEntity(currentEntry.getKey());
                return account.toBuilder()
                    .subAccounts(subAccounts)
                    .build();
            }).collect(Collectors.toList());
    }

}
