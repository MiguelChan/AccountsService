package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountRequest;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
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
public class DeleteAccountTests extends BaseTests {

    private static final String TEST_USER = "DltAcctTestUser";

    @Test
    public void deleteAccount_should_deleteAccount() throws Exception {
        String accountId = createRandomAccounts();

        DeleteAccountRequest request = DeleteAccountRequest.builder()
            .accountId(accountId)
            .build();

        DeleteAccountResponse response = this.serviceClient.deleteAccount(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getDeletedAccount());
    }

    private String createRandomAccounts() throws Exception {
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

        return response.getAccountId();
    }

}
