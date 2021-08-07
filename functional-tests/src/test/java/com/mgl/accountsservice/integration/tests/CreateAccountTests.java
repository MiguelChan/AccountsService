package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.models.Account;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.testng.annotations.Test;

/**
 * .
 */
@Test
public class CreateAccountTests extends BaseTests {

    private static final String TEST_USER = "FuncTestUser";

    @Test
    public void createAccount_should_createWithSubAccounts() throws Exception {
        Account randomAccount = EnhancedRandom.random(Account.class);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .account(randomAccount)
            .requestingUser(TEST_USER)
            .build();

        CreateAccountResponse response = this.serviceClient.createAccount(request);

        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

}
