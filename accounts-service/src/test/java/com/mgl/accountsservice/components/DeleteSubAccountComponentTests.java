package com.mgl.accountsservice.components;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class DeleteSubAccountComponentTests {

    private static final String TEST_SUB_ACCOUNT_ID = "12345";

    @Mock
    private SubAccountsDao subAccountsDao;
    @Mock
    private SubAccountsEntityMapper subAccountsEntityMapper;

    private DeleteSubAccountComponent component;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        component = new DeleteSubAccountComponent(subAccountsDao, subAccountsEntityMapper);
    }

    @Test
    public void deleteSubAccount_should_deleteTheSubAccount() {
        SubAccountEntity randomSubAccount = EnhancedRandom.random(SubAccountEntity.class);
        when(subAccountsDao.getSubAccount(TEST_SUB_ACCOUNT_ID)).thenReturn(randomSubAccount);

        SubAccount expectedSubAccount = EnhancedRandom.random(SubAccount.class);
        when(subAccountsEntityMapper.fromEntity(randomSubAccount)).thenReturn(expectedSubAccount);

        Optional<SubAccount> subAccount = component.deleteSubAccount(TEST_SUB_ACCOUNT_ID);

        assertThat(subAccount.isEmpty()).isFalse();
        assertThat(subAccount.get()).isEqualTo(expectedSubAccount);
        verify(subAccountsDao).deleteSubAccount(TEST_SUB_ACCOUNT_ID);
    }

    @Test
    public void deleteSubAccount_should_returnEmpty_when_subAccountDoesNotExist() {
        when(subAccountsDao.getSubAccount(TEST_SUB_ACCOUNT_ID)).thenThrow(DatabaseException.class);

        Optional<SubAccount> foundSubAccount = component.deleteSubAccount(TEST_SUB_ACCOUNT_ID);

        assertThat(foundSubAccount.isEmpty()).isTrue();
        verifyNoMoreInteractions(subAccountsDao);
    }

    @Test
    public void deleteSubAccount_should_bubbleUpExceptions_when_daoFails() {
        SubAccountEntity randomSubAccount = EnhancedRandom.random(SubAccountEntity.class);
        when(subAccountsDao.getSubAccount(TEST_SUB_ACCOUNT_ID)).thenReturn(randomSubAccount);

        SubAccount expectedSubAccount = EnhancedRandom.random(SubAccount.class);
        when(subAccountsEntityMapper.fromEntity(randomSubAccount)).thenReturn(expectedSubAccount);

        doThrow(DatabaseException.class).when(subAccountsDao).deleteSubAccount(TEST_SUB_ACCOUNT_ID);

        assertThatThrownBy(() -> component.deleteSubAccount(TEST_SUB_ACCOUNT_ID))
            .isInstanceOfAny(DatabaseException.class);
    }

}
