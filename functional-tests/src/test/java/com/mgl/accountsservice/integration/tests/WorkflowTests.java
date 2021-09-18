package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.beust.jcommander.internal.Lists;
import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountRequest;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.GetAccountByIdRequest;
import com.mgl.accountsservice.dto.GetAccountByIdResponse;
import com.mgl.accountsservice.dto.GetAccountsRequest;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PutAccountRequest;
import com.mgl.accountsservice.dto.PutAccountResponse;
import com.mgl.accountsservice.models.Account;
import com.mgl.accountsservice.models.AccountType;
import com.mgl.accountsservice.models.SubAccount;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.util.List;
import java.util.Optional;
import org.testng.ITestContext;
import org.testng.annotations.Test;

/**
 * .
 */
@Test(groups = "workflowTests")
public class WorkflowTests extends BaseTests {

    private static final String TEST_USER = "WorkflowUser";

    private static final String KEY_ACCOUNT_ID = "AccountId";
    private static final String KEY_ACCOUNT_OBJECT = "AccountObject";

    @Test
    public void createAnAccount_should_createAnAccount(ITestContext testContext) throws Exception {
        Account account = EnhancedRandom.random(Account.class, "id", "subAccounts");
        SubAccount subAccount = EnhancedRandom.random(SubAccount.class, "id");

        account.setSubAccounts(Lists.newArrayList(subAccount));

        CreateAccountRequest createRequest = CreateAccountRequest.builder()
            .account(account)
            .build();

        CreateAccountResponse response = this.serviceClient.createAccount(createRequest);

        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertTrue(response.isSuccess());

        testContext.setAttribute(KEY_ACCOUNT_ID, response.getAccountId());
    }

    @Test(dependsOnMethods = "createAnAccount_should_createAnAccount")
    public void getAccount_should_getAnAccount(ITestContext testContext) throws Exception {
        String accountId = (String) testContext.getAttribute(KEY_ACCOUNT_ID);

        Account createdAccount = getAccount(accountId);

        assertEquals(accountId, createdAccount.getId());
        assertNotNull(createdAccount.getSubAccounts());
        assertFalse(createdAccount.getSubAccounts().isEmpty());
        assertEquals(1, createdAccount.getSubAccounts().size());

        testContext.setAttribute(KEY_ACCOUNT_OBJECT, createdAccount);
    }

    @Test(dependsOnMethods = "getAccount_should_getAnAccount")
    public void getAccounts_should_getAccounts(ITestContext testContext) throws Exception {
        String accountId = (String) testContext.getAttribute(KEY_ACCOUNT_ID);

        GetAccountsRequest getRequest = GetAccountsRequest.builder()
            .build();

        GetAccountsResponse response = this.serviceClient.getAccounts(getRequest);

        assertNotNull(response);
        assertNotNull(response.getAccounts());

        Optional<Account> foundAccount = response.getAccounts()
            .stream()
            .filter(currentAccount -> currentAccount.getId().equals(accountId))
            .findFirst();

        assertTrue(foundAccount.isPresent());
    }

    @Test(dependsOnMethods = "getAccounts_should_getAccounts")
    public void putAccount_should_updateTheAccount(ITestContext testContext) throws Exception {
        String accountId = (String) testContext.getAttribute(KEY_ACCOUNT_ID);

        Account accountToUpdate = getAccount(accountId);
        accountToUpdate.setAccountType(AccountType.Expenses);
        accountToUpdate.setTitle("Updated!");

        SubAccount newSubAccount = SubAccount.builder()
            .description("NewAccount")
            .build();

        SubAccount subAccountToEdit = accountToUpdate.getSubAccounts().get(0);
        subAccountToEdit.setDescription("EditedSubAccount");

        List<SubAccount> subAccounts = Lists.newArrayList(newSubAccount, subAccountToEdit);

        accountToUpdate = accountToUpdate.toBuilder()
            .subAccounts(subAccounts)
            .build();

        PutAccountRequest putRequest = PutAccountRequest.builder()
            .updatedAccount(accountToUpdate)
            .updatingUser(TEST_USER)
            .build();

        PutAccountResponse putResponse = this.serviceClient.putAccount(putRequest);

        assertNotNull(putResponse);
        assertTrue(putResponse.isSuccess());

        Account updatedAccount = getAccount(accountId);
        assertEquals(updatedAccount.getTitle(), accountToUpdate.getTitle());
        assertEquals(updatedAccount.getSubAccounts().size(), 2);

        List<SubAccount> updatedSubAccounts = updatedAccount.getSubAccounts();
        Optional<SubAccount> foundSubAccount = updatedSubAccounts.stream()
            .filter(currentSubAccount -> currentSubAccount.getDescription().equals(newSubAccount.getDescription()))
            .findFirst();
        assertTrue(foundSubAccount.isPresent());

        foundSubAccount = updatedSubAccounts.stream()
            .filter(currentSubAccount -> currentSubAccount.getDescription().equals(subAccountToEdit.getDescription()))
            .findFirst();
        assertTrue(foundSubAccount.isPresent());
    }

    @Test(dependsOnMethods = "putAccount_should_updateTheAccount")
    public void deleteAccount_should_delete(ITestContext testContext) throws Exception {
        String accountId = (String) testContext.getAttribute(KEY_ACCOUNT_ID);

        DeleteAccountRequest request = DeleteAccountRequest.builder()
            .accountId(accountId)
            .build();

        DeleteAccountResponse response = this.serviceClient.deleteAccount(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());

        GetAccountByIdRequest getRequest = GetAccountByIdRequest.builder()
            .accountId(accountId)
            .build();

        GetAccountByIdResponse getByResponse = this.serviceClient.getAccount(getRequest);
        assertNotNull(getByResponse);
        assertFalse(getByResponse.isSuccess());
    }

    private Account getAccount(String accountId) throws Exception {
        GetAccountByIdRequest getByIdRequest = GetAccountByIdRequest.builder()
            .accountId(accountId)
            .build();

        GetAccountByIdResponse getByIdResponse = this.serviceClient.getAccount(getByIdRequest);

        assertNotNull(getByIdResponse);
        assertNotNull(getByIdResponse.getAccount());

        return getByIdResponse.getAccount();
    }

}
