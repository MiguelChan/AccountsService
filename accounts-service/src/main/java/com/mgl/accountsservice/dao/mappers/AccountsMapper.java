package com.mgl.accountsservice.dao.mappers;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * .
 */
@Mapper
public interface AccountsMapper {

    /**
     * .
     *
     * @param accountEntity .
     */
    void insertAccount(AccountEntity accountEntity);

}
