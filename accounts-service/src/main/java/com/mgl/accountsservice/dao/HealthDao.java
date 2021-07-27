package com.mgl.accountsservice.dao;

/**
 * An interface for connecting to a DAO for Health monitoring.
 */
public interface HealthDao {

    /**
     * Returns true when the DB connection is healthy, false otherwise.
     *
     * @return .
     */
    boolean isHealthy();

}
