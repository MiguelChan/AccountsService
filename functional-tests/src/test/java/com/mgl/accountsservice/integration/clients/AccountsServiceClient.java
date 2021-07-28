package com.mgl.accountsservice.integration.clients;

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

}
