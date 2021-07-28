package com.mgl.accountsservice.integration.clients.impl;

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

    private <T> T makeCall(Call<T> callMethod) throws Exception {
        Response<T> callResponse = callMethod.execute();
        return callResponse.body();
    }

}
