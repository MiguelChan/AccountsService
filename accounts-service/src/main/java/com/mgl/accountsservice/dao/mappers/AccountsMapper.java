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

    /**
     * .
     *
     * @param accountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    AccountEntity getAccount(String accountId) throws DatabaseException;

    /**
     * .
     *
     * @param accountId .
     *
     * @throws DatabaseException .
     */
    void deleteAccount(String accountId) throws DatabaseException;

    /**
     * .
     *
     * @param accountEntity .
     *
     * @throws DatabaseException .
     */
    void putAccount(AccountEntity accountEntity) throws DatabaseException;

}
