package com.mgl.accountsservice.integration.clients.impl;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.PingResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}
