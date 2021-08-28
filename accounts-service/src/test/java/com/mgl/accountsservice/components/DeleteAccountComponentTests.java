package com.mgl.accountsservice.components;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.AccountsDao;
import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.AccountsEntityMapper;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Optional;
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
public class DeleteAccountComponentTests {

    private static final String TEST_ACCOUNT_ID = "SomeId";

    @Mock
    private AccountsDao accountsDao;
    @Mock
    private SubAccountsDao subAccountsDao;
    @Mock
    private AccountsEntityMapper accountsEntityMapper;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;

    private DeleteAccountComponent component;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        component = new DeleteAccountComponent(
            accountsDao,
            accountsEntityMapper,
            subAccountsDao,
            subAccountsEntityMapper
        );
    }

    @Test
    public void deleteAccount_should_deleteTheAccount() {
        AccountEntity accountEntity = EnhancedRandom.random(AccountEntity.class);
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenReturn(accountEntity);

        SubAccountEntity subAccountEntity = EnhancedRandom.random(SubAccountEntity.class);
        when(subAccountsDao.getSubAccounts(TEST_ACCOUNT_ID)).thenReturn(Lists.newArrayList(subAccountEntity));

        Account account = EnhancedRandom.random(Account.class, "subAccounts");
        when(accountsEntityMapper.fromEntity(accountEntity)).thenReturn(account);

        SubAccount subAccount = EnhancedRandom.random(SubAccount.class);
        when(subAccountsEntityMapper.fromEntity(subAccountEntity)).thenReturn(subAccount);

        Optional<Account> deletedAccount = component.deleteAccount(TEST_ACCOUNT_ID);

        assertThat(deletedAccount.isPresent()).isTrue();

        Account finalAccount = account.toBuilder()
            .subAccounts(Lists.newArrayList(subAccount))
            .build();

        assertThat(deletedAccount.get()).isEqualTo(finalAccount);
    }

    @Test
    public void deleteAccount_should_returnEmpty_when_accountDoesNotExist() {
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenReturn(null);

        Optional<Account> deletedAccount = component.deleteAccount(TEST_ACCOUNT_ID);

        assertThat(deletedAccount.isEmpty()).isTrue();
    }

    @Test
    public void deleteAccount_should_returnEmpty_when_anErrorOccurs() {
        when(accountsDao.getAccount(TEST_ACCOUNT_ID)).thenThrow(DatabaseException.class);

        Optional<Account> deletedAccount = component.deleteAccount(TEST_ACCOUNT_ID);

        assertThat(deletedAccount.isEmpty()).isTrue();
    }

}
