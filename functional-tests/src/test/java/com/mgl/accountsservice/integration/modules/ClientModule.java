package com.mgl.accountsservice.integration.modules;

import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mgl.accountsservice.integration.clients.AccountsServiceClient;
import com.mgl.accountsservice.integration.clients.impl.RetrofitAccountsClient;
import com.mgl.accountsservice.integration.clients.impl.RetrofitAccountsServiceClient;
import java.time.LocalDateTime;
import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Guice Module for connecting to Clients.
 */
public class ClientModule extends AbstractModule {

    private static final String BASE_URL = "http://localhost:8091/api/";

    /**
     * .
     */
    @Override
    protected void configure() {
        bind(AccountsServiceClient.class).to(RetrofitAccountsServiceClient.class);
    }

    /**
     * .
     *
     * @return .
     */
    @Provides
    @Singleton
    public RetrofitAccountsClient providesRetrofitAccountsClient() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build();

        return retrofit.create(RetrofitAccountsClient.class);
    }
}
