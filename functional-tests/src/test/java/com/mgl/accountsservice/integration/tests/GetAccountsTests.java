package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.GetAccountsRequest;
import com.mgl.accountsservice.dto.GetAccountsResponse;
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
public class GetAccountsTests extends BaseTests {

    private static final String TEST_USER = "FncTestsGetAccts";

    @Test
    public void getAccounts_should_getAllAccounts() throws Exception {
        createRandomAccounts();

        GetAccountsRequest request = GetAccountsRequest.builder().build();
        GetAccountsResponse response = this.serviceClient.getAccounts(request);

        assertNotNull(response);

        List<Account> allAccounts = response.getAccounts();

        assertFalse(allAccounts.isEmpty());
    }

    private void createRandomAccounts() throws Exception {
        Account account = EnhancedRandom.random(Account.class);
        account = dataCurator.curateAccountData(account);

        List<SubAccount> subAccountList = account.getSubAccounts()
            .stream()
            .map(dataCurator::curateSubAccount)
            .collect(Collectors.toList());

        account.setSubAccounts(subAccountList);

        CreateAccountRequest request = CreateAccountRequest.builder()
            .requestingUser(TEST_USER)
            .account(account)
            .build();

        CreateAccountResponse response = this.serviceClient.createAccount(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

}
