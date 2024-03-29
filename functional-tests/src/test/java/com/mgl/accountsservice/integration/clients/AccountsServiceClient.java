package com.mgl.accountsservice.integration.clients;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountRequest;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.DeleteSubAccountRequest;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.dto.GetAccountByIdRequest;
import com.mgl.accountsservice.dto.GetAccountByIdResponse;
import com.mgl.accountsservice.dto.GetAccountsRequest;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PingResponse;
import com.mgl.accountsservice.dto.PutAccountRequest;
import com.mgl.accountsservice.dto.PutAccountResponse;

/**
 * Defines the AccountsService Client.
 */
public interface AccountsServiceClient {

    /**
     * Pings the Client.
     *
     * @return A {@link PingResponse} that contains the result.
     *
     * @throws Exception thrown after if an error occurs.
     */
    PingResponse ping() throws Exception;

    /**
     * DeepPings the Client.
     *
     * @return A {@link PingResponse} that contains the result.
     *
     * @throws Exception thrown after if an error occurs.
     */
    PingResponse deepPing() throws Exception;

    /**
     * Creates an Account.
     *
     * @param request The request containing all the information for creating an account.
     *
     * @return A {@link CreateAccountResponse}.
     *
     * @throws Exception .
     */
    CreateAccountResponse createAccount(CreateAccountRequest request) throws Exception;

    /**
     * Gets a List of all the Accounts.
     *
     * @param request .
     *
     * @return .
     *
     * @throws Exception .
     */
    GetAccountsResponse getAccounts(GetAccountsRequest request) throws Exception;

    /**
     * Deletes an Account based off its accountId.
     *
     * @param request .
     *
     * @return .
     *
     * @throws Exception .
     */
    DeleteAccountResponse deleteAccount(DeleteAccountRequest request) throws Exception;

    /**
     * Deletes a SubAccount based off its subAccountId.
     *
     * @param request .
     *
     * @return .
     *
     * @throws Exception .
     */
    DeleteSubAccountResponse deleteSubAccount(DeleteSubAccountRequest request) throws Exception;

    /**
     * Gets an {@link com.mgl.accountsservice.models.Account} by its Id.
     *
     * @param request .
     *
     * @return .
     *
     * @throws Exception .
     */
    GetAccountByIdResponse getAccount(GetAccountByIdRequest request) throws Exception;

    /**
     * Puts a {@link com.mgl.accountsservice.models.Account} and its related
     * {@link com.mgl.accountsservice.models.SubAccount}s.*
     * This method also creates new {@link com.mgl.accountsservice.models.SubAccount} if they are
     * supplied.
     *
     * @param request .
     *
     * @return .
     *
     * @throws Exception .
     */
    PutAccountResponse putAccount(PutAccountRequest request) throws Exception;

}
