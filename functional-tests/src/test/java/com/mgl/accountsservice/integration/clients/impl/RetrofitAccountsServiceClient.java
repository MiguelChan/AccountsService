package com.mgl.accountsservice.integration.clients.impl;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountRequest;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.DeleteSubAccountRequest;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.dto.GetAccountsRequest;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PingResponse;
import com.mgl.accountsservice.integration.clients.AccountsServiceClient;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Response;

/**
 * The retrofit implementation.
 */
@Singleton
public class RetrofitAccountsServiceClient implements AccountsServiceClient {

    private final RetrofitAccountsClient retrofitAccountsClient;

    @Inject
    public RetrofitAccountsServiceClient(RetrofitAccountsClient retrofitAccountsClient) {
        this.retrofitAccountsClient = retrofitAccountsClient;
    }

    @Override
    public PingResponse ping() throws Exception {
        return makeCall(retrofitAccountsClient.ping());
    }

    @Override
    public PingResponse deepPing() throws Exception {
        return makeCall(retrofitAccountsClient.deepPing());
    }

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) throws Exception {
        return makeCall(retrofitAccountsClient.createAccount(request));
    }

    @Override
    public GetAccountsResponse getAccounts(GetAccountsRequest request) throws Exception {
        return makeCall(retrofitAccountsClient.getAccounts());
    }

    @Override
    public DeleteAccountResponse deleteAccount(DeleteAccountRequest request) throws Exception {
        String accountId = request.getAccountId();
        return makeCall(retrofitAccountsClient.deleteAccount(accountId));
    }

    @Override
    public DeleteSubAccountResponse deleteSubAccount(DeleteSubAccountRequest request)
        throws Exception {
        String subAccountId = request.getSubAccountId();
        return makeCall(retrofitAccountsClient.deleteSubAccount(subAccountId));
    }

    private <T> T makeCall(Call<T> callMethod) throws Exception {
        Response<T> callResponse = callMethod.execute();
        return callResponse.body();
    }

}
