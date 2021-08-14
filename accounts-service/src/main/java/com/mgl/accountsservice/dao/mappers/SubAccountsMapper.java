package com.mgl.accountsservice.dao.mappers;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis Mapper for {@link com.mgl.accountsservice.models.SubAccount}s.
 */
@Mapper
public interface SubAccountsMapper {

    /**
     * .
     *
     * @param subAccountEntity .
     *
     * @throws DatabaseException .
     */
    void insertSubAccount(SubAccountEntity subAccountEntity) throws DatabaseException;

}
