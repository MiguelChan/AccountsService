package com.mgl.accountsservice.dao.mappers;

import org.apache.ibatis.annotations.Mapper;

/**
 * .
 */
@Mapper
public interface HealthMapper {

    /**
     * Whether the DB is healthy or not.
     *
     * @return true if the DB is healthy, false otherwise.
     */
    boolean isHealthy();

}
