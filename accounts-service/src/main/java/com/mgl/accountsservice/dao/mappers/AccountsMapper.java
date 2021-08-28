package com.mgl.accountsservice.dao.mappers;

import com.mgl.accountsservice.dao.entities.AccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import java.util.List;
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

    /**
     * .
     *
     * @return .
     */
    List<AccountEntity> getAccounts() throws DatabaseException;

}
