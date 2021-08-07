package com.mgl.accountsservice.integration.clients;

import com.mgl.accountsservice.dto.CreateAccountRequest;
import com.mgl.accountsservice.dto.CreateAccountResponse;
import com.mgl.accountsservice.dto.PingResponse;

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

}
