package com.mgl.accountsservice.integration.clients.impl;

import com.mgl.accountsservice.dto.PingResponse;
import retrofit2.Call;
import retrofit2.http.GET;

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
}
