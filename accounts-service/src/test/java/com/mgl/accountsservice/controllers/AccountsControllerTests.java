package com.mgl.accountsservice.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.mgl.accountsservice.components.CreateAccountComponent;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.models.Account;
import io.github.benas.randombeans.api.EnhancedRandom;
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

    private AccountsController accountsController;

    @BeforeEach
    public void setup() {
        accountsController = new AccountsController(createAccountComponent);
    }

    @Test
    public void createAccount_should_returnSuccess() {
        Account account = EnhancedRandom.random(Account.class);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .account(account)
            .requestingUser(TEST_USER)
            .build();

        CreateAccountResponse response = accountsController.createAccount(request);

        assertThat(response.isSuccess()).isTrue();
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

}
