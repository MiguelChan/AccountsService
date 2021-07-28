package com.mgl.accountsservice.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.mgl.accountsservice.dao.HealthDao;
import com.mgl.accountsservice.dto.PingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * .
 */
@ExtendWith(MockitoExtension.class)
public class HealthControllerTests {

    @Mock private HealthDao healthDao;

    private HealthController healthController;

    @BeforeEach
    public void setup() {
        healthController = new HealthController(healthDao);
    }

    @Test
    public void ping_should_returnTrue() {
        PingResponse pingResponse = healthController.ping();
        assertThat(pingResponse.isHealthy()).isTrue();
    }

    @Test
    public void deepPing_should_returnTrue() {
        when(healthDao.isHealthy()).thenReturn(true);

        PingResponse pingResponse = healthController.deepPing();
        assertThat(pingResponse.isHealthy()).isTrue();
    }

}
