package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import java.util.stream.Collectors;
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
        randomAccount = dataCurator.curateAccountData(randomAccount);

        List<SubAccount> curatedSubAccounts = randomAccount.getSubAccounts()
            .stream()
            .map(dataCurator::curateSubAccount)
            .collect(Collectors.toList());

        randomAccount.setSubAccounts(curatedSubAccounts);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .account(randomAccount)
            .requestingUser(TEST_USER)
            .build();

        CreateAccountResponse response = this.serviceClient.createAccount(request);

        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getAccountId());
        assertFalse(response.getAccountId().isEmpty());
    }

}
