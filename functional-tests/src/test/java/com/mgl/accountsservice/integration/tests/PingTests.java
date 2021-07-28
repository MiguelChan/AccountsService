package com.mgl.accountsservice.integration.tests;

import static org.testng.AssertJUnit.assertTrue;

import com.mgl.accountsservice.dto.PingResponse;
import org.testng.annotations.Test;

/**
 * Tests for Ping's Operations.
 */
@Test
public class PingTests extends BaseTests {

    @Test
    public void ping_should_returnTrue() throws Exception {
        PingResponse pingResponse = this.serviceClient.ping();
        assertTrue(pingResponse.isHealthy());
    }

    @Test
    public void deepPing_should_returnTrue() throws Exception {
        PingResponse pingResponse = this.serviceClient.deepPing();
        assertTrue(pingResponse.isHealthy());
    }

}
