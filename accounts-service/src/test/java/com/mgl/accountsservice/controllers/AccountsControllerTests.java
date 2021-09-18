package com.mgl.accountsservice.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.components.DeleteAccountComponent;
import com.mgl.accountsservice.components.GetAccountByIdComponent;
import com.mgl.accountsservice.components.GetAccountsComponent;
import com.mgl.accountsservice.components.PutAccountComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.GetAccountByIdResponse;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PutAccountRequest;
import com.mgl.accountsservice.dto.PutAccountResponse;
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
    @Mock
    private GetAccountByIdComponent getAccountByIdComponent;
    @Mock
    private PutAccountComponent putAccountComponent;

    private AccountsController accountsController;

    /**
     * .
     */
    @BeforeEach
    public void setup() {
        accountsController = new AccountsController(
            createAccountComponent,
            getAccountsComponent,
            deleteAccountComponent,
            getAccountByIdComponent,
            putAccountComponent
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

    @Test
    public void getAccountById_should_returnAccount() {
        String testAccountId = "SomeSome";

        Account expectedAccount = EnhancedRandom.random(Account.class);

        when(getAccountByIdComponent.getAccount(testAccountId))
            .thenReturn(Optional.of(expectedAccount));

        GetAccountByIdResponse response = accountsController.getAccount(testAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();

        Account foundAccount = response.getAccount();
        assertThat(foundAccount).isEqualTo(expectedAccount);
    }

    @Test
    public void getAccountById_should_returnEmptyResponse_when_accountDoesNotExist() {
        String testAccountId = "SomeSome";

        when(getAccountByIdComponent.getAccount(testAccountId)).thenReturn(Optional.empty());

        GetAccountByIdResponse response = accountsController.getAccount(testAccountId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getAccount()).isNull();
    }

    @Test
    public void getAccountById_should_returnEmptyResponse_when_componentFails() {
        String testAccountId = "SomeSome";
        String expectedErrorMessage = "There was an unexpected error";

        RuntimeException exception = new RuntimeException(expectedErrorMessage);

        when(getAccountByIdComponent.getAccount(testAccountId)).thenThrow(exception);

        GetAccountByIdResponse response = accountsController.getAccount(testAccountId);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void putAccount_should_put() {
        String updatingUser = EnhancedRandom.random(String.class);
        Account accountToUpdate = EnhancedRandom.random(Account.class);
        when(putAccountComponent.putAccount(accountToUpdate, updatingUser))
            .thenReturn(accountToUpdate);

        PutAccountRequest request = PutAccountRequest.builder()
            .updatedAccount(accountToUpdate)
            .updatingUser(updatingUser)
            .build();

        PutAccountResponse response = accountsController.putAccount(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getUpdatedAccount()).isEqualTo(accountToUpdate);
    }

    @Test
    public void putAccount_should_returnUnsuccessfulResponse_uponFailure() {
        String errorMessage = "SomeErrorMessage";
        RuntimeException exception = new RuntimeException(errorMessage);

        when(putAccountComponent.putAccount(any(), any())).thenThrow(exception);

        PutAccountRequest request = PutAccountRequest.builder()
            .updatingUser("SomeSome")
            .updatedAccount(Account.builder().build())
            .build();

        PutAccountResponse response = accountsController.putAccount(request);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(errorMessage);
    }

}
