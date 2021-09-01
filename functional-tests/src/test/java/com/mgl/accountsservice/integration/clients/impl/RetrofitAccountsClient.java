package com.mgl.accountsservice.integration.clients.impl;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.DeleteAccountResponse;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.dto.GetAccountsResponse;
import com.mgl.accountsservice.dto.PingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Defines the underlying Retrofit Client for the AccountsService.
 */
public interface RetrofitAccountsClient {

    /**
     * .
     *
     * @return .
     */
    @GET("ping")
    Call<PingResponse> ping();

    /**
     * .
     *
     * @return .
     */
    @GET("deep_ping")
    Call<PingResponse> deepPing();

    /**
     * .
     *
     * @param request .
     *
     * @return .
     */
    @POST("accounts")
    Call<CreateAccountResponse> createAccount(@Body CreateAccountRequest request);

    /**
     * .
     *
     * @return .
     */
    @GET("accounts")
    Call<GetAccountsResponse> getAccounts();

    /**
     * .
     *
     * @param accountId .
     *
     * @return .
     */
    @DELETE("accounts/{accountId}")
    Call<DeleteAccountResponse> deleteAccount(@Path("accountId") String accountId);

    /**
     * .
     *
     * @param subAccountId .
     *
     * @return .
     */
    @DELETE("subAccounts/{subAccountId}")
    Call<DeleteSubAccountResponse> deleteSubAccount(@Path("subAccountId") String subAccountId);
}
