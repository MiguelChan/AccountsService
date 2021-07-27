package com.mgl.accountsservice.dao.impl;

import com.mgl.accountsservice.dao.HealthDao;
import com.mgl.accountsservice.dao.mappers.HealthMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * .
 */
@Log4j2
@Component
public class MyBatisHealthDao implements HealthDao {

    private final HealthMapper healthMapper;

    @Autowired
    public MyBatisHealthDao(HealthMapper healthMapper) {
        this.healthMapper = healthMapper;
    }

    @Override
    public boolean isHealthy() {
        try {
            return healthMapper.isHealthy();
        } catch (Exception e) {
            log.error("An error occurred while trying to query the Database", e);
            return false;
        }
    }
}
