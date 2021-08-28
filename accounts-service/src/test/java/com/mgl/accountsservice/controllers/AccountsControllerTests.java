package com.mgl.accountsservice.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.components.DeleteAccountComponent;
import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.models.Account;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
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
public class AccountsControllerTests {

    private static final String TEST_USER = "SomeRandomUser";

    @Mock
    private CreateAccountComponent createAccountComponent;
    @Mock
    private GetAccountsComponent getAccountsComponent;
    @Mock
    private DeleteAccountComponent deleteAccountComponent;

    private AccountsController accountsController;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        accountsController = new AccountsController(
            createAccountComponent,
            getAccountsComponent,
            deleteAccountComponent
        );
    }

    @Test
    public void createAccount_should_returnSuccess() {
        Account account = EnhancedRandom.random(Account.class);
        String accountId = EnhancedRandom.random(String.class);

        when(createAccountComponent.createAccount(account, TEST_USER)).thenReturn(accountId);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .account(account)
            .requestingUser(TEST_USER)
            .build();

        CreateAccountResponse response = accountsController.createAccount(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAccountId()).isEqualTo(accountId);
        verify(createAccountComponent).createAccount(account, TEST_USER);
    }

    @Test
    public void createAccount_should_returnErrorMessage_when_errorOccurs() {
        String expectedErrorMessage = "SomeRandomErrorMessage";
        RuntimeException mockException = new RuntimeException(expectedErrorMessage);
        doThrow(mockException).when(createAccountComponent).createAccount(any(), any());

        Account account = EnhancedRandom.random(Account.class);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .requestingUser(TEST_USER)
            .account(account)
            .build();

        CreateAccountResponse response = accountsController.createAccount(request);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(expectedErrorMessage);
        verify(createAccountComponent).createAccount(account, TEST_USER);
    }

    @Test
    public void getAccounts_should_getAllAccounts() {
        List<Account> randomAccounts = EnhancedRandom.randomListOf(5, Account.class);

        when(getAccountsComponent.getAccounts()).thenReturn(randomAccounts);

        GetAccountsResponse response = accountsController.getAccounts();

        assertThat(response).isNotNull();
        assertThat(response.getAccounts()).isEqualTo(randomAccounts);
    }

    @Test
    public void getAccounts_should_returnErrorMessage_when_errorOccurs() {
        String errorMessage = "SomeException";
        RuntimeException runtimeException = new RuntimeException(errorMessage);

        when(getAccountsComponent.getAccounts()).thenThrow(runtimeException);

        GetAccountsResponse response = accountsController.getAccounts();

        assertThat(response).isNotNull();
        assertThat(response.getAccounts()).isNull();
        assertThat(response.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    public void deleteAccount_should_deleteAccount() {
        String testAccountId = "SomeId";

        Account account = EnhancedRandom.random(Account.class);
        when(deleteAccountComponent.deleteAccount(testAccountId)).thenReturn(Optional.of(account));

        DeleteAccountResponse response = accountsController.deleteAccount(testAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getDeletedAccount()).isEqualTo(account);
    }

    @Test
    public void deleteAccount_should_bubbleUpException() {
        String testAccountId = "SomeId";
        when(deleteAccountComponent.deleteAccount(testAccountId))
            .thenThrow(DatabaseException.class);

        DeleteAccountResponse response = accountsController.deleteAccount(testAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getDeletedAccount()).isNull();
    }

    @Test
    public void deleteAccount_should_returnUnsuccessfulMessage_when_accountIsNotPresent() {
        String testAccountId = "SomeSome";

        when(deleteAccountComponent.deleteAccount(testAccountId)).thenReturn(Optional.empty());

        DeleteAccountResponse response = accountsController.deleteAccount(testAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getDeletedAccount()).isNull();
    }
}
