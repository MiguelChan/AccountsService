package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteSubAccountRequest;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
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
public class DeleteSubAccountTests extends BaseTests {

    private static final String TEST_USER = "DltSubAcctUser";

    @Test
    public void deleteSubAccount_should_deleteaSubAccount() throws Exception {
        String accountId = createRandomAccounts();

        Account foundAccount = getAccount(accountId);

        SubAccount randomSubAccount = foundAccount.getSubAccounts()
            .stream()
            .findAny()
            .get();

        DeleteSubAccountRequest deleteRequest = DeleteSubAccountRequest.builder()
            .subAccountId(randomSubAccount.getId())
            .build();

        DeleteSubAccountResponse deleteResponse = this.serviceClient.deleteSubAccount(deleteRequest);

        assertNotNull(deleteResponse);
        assertTrue(deleteResponse.isSuccess());
        assertEquals(deleteResponse.getDeletedSubAccount(), randomSubAccount);
    }

    private Account getAccount(String accountId) throws Exception {
        GetAccountsRequest request = GetAccountsRequest.builder().build();

        GetAccountsResponse response = this.serviceClient.getAccounts(request);

        return response.getAccounts()
            .stream()
            .filter(currentAccount -> currentAccount.getId().equals(accountId))
            .findFirst()
            .get();
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
