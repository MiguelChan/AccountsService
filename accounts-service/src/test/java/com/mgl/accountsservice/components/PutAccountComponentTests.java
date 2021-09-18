package com.mgl.accountsservice.components;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class PutAccountComponentTests {

    private static final String TEST_USER = "SomeUser";

    @Mock
    private AccountsDao accountsDao;
    @Mock
    private AccountsEntityMapper accountsEntityMapper;
    @Mock
    private SubAccountsDao subAccountsDao;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;

    private PutAccountComponent component;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        component = new PutAccountComponent(
            accountsDao,
            accountsEntityMapper,
            subAccountsDao,
            subAccountsEntityMapper
        );
    }

    @Test
    public void putAccount_should_throwNpe_when_accountIsNull() {
        assertThatThrownBy(() -> component.putAccount(null, TEST_USER)).isInstanceOfAny(Exception.class);
    }

    @Test
    public void putAccount_should_throwNpe_when_accountIdIsNull() {
        Account account = Account.builder()
            .build();

        assertThatThrownBy(() -> component.putAccount(account, TEST_USER)).isInstanceOfAny(Exception.class);
    }

    @Test
    public void putAccount_should_putAccount() {
        // 1.- Setting input.
        Account account = EnhancedRandom.random(Account.class, "subAccounts");
        SubAccount existingSubAccount = EnhancedRandom.random(SubAccount.class);
        SubAccount newSubAccount = EnhancedRandom.random(SubAccount.class, "id");

        List<SubAccount> subAccounts = Lists.newArrayList(existingSubAccount, newSubAccount);
        account.setSubAccounts(subAccounts);

        // 2.- Setting Mappers
        SubAccountEntity existingSubAccountEntity = EnhancedRandom.random(SubAccountEntity.class);
        SubAccountEntity newSubAccountEntity = EnhancedRandom.random(SubAccountEntity.class, "id");

        when(subAccountsEntityMapper.fromModel(existingSubAccount)).thenReturn(existingSubAccountEntity);
        when(subAccountsEntityMapper.fromModel(newSubAccount)).thenReturn(newSubAccountEntity);

        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        when(accountsEntityMapper.fromModel(account)).thenReturn(accountEntity);

        SubAccount randomSubAccount = EnhancedRandom.random(SubAccount.class);
        when(subAccountsEntityMapper.fromEntity(any())).thenReturn(randomSubAccount);

        when(accountsEntityMapper.fromEntity(accountEntity)).thenReturn(account);

        // 3.- Setting up DAO's
        when(accountsDao.getAccount(any())).thenReturn(accountEntity);
        when(subAccountsDao.getSubAccounts(any())).thenReturn(Lists.newArrayList(existingSubAccountEntity, newSubAccountEntity));

        Account updatedAccount = component.putAccount(account, TEST_USER);

        Account expectedAccount = account.toBuilder()
            .subAccounts(Lists.newArrayList(randomSubAccount, randomSubAccount))
            .build();

        assertThat(updatedAccount).isEqualTo(expectedAccount);
        verify(subAccountsDao).putSubAccount(existingSubAccountEntity);
        verify(subAccountsDao).insertSubAccount(newSubAccountEntity);
        verify(accountsDao).putAccount(accountEntity);
    }

}
