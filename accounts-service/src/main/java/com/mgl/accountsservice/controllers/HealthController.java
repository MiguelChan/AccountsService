package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.dao.HealthDao;
import com.mgl.accountsservice.dto.PingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Defines the REST Controller for Status and Health monitoring.
 */
@RestController
@RequestMapping("/")
public class HealthController {

    private final HealthDao healthDao;

    @Autowired
    public HealthController(HealthDao healthDao) {
        this.healthDao = healthDao;
    }

    /**
     * A ping operation. Returns true unless there is something wrong and we
     * can't reach the service.
     *
     * @return true.
     */
    @GetMapping
    @RequestMapping("/ping")
    public PingResponse ping() {
        return PingResponse.builder()
            .healthy(true)
            .build();
    }

    /**
     * Performs a Deep Ping.
     *
     * @return true when dependencies are healthy, false otherwise.
     */
    @GetMapping
    @RequestMapping("/deep_ping")
    public PingResponse deepPing() {
        boolean isHealthy = healthDao.isHealthy();
        return PingResponse.builder()
            .healthy(isHealthy)
            .build();
    }

}
