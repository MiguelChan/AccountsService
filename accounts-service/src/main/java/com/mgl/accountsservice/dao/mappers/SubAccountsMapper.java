package com.mgl.accountsservice.dao.mappers;

import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import java.util.List;
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

    /**
     * .
     *
     * @param accountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    List<SubAccountEntity> getSubAccounts(String accountId) throws DatabaseException;

    /**
     * .
     *
     * @param subAccountId .
     *
     * @throws DatabaseException .
     */
    void deleteSubAccount(String subAccountId) throws DatabaseException;

    /**
     * .
     *
     * @param subAccountId .
     *
     * @return .
     *
     * @throws DatabaseException .
     */
    SubAccountEntity getSubAccount(String subAccountId) throws DatabaseException;

    /**
     * .
     *
     * @param subAccountEntity .
     *
     * @throws DatabaseException .
     */
    void putSubAccount(SubAccountEntity subAccountEntity) throws DatabaseException;

}
