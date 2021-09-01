package com.mgl.accountsservice.components;

import com.mgl.accountsservice.dao.SubAccountsDao;
import com.mgl.accountsservice.dao.entities.SubAccountEntity;
import com.mgl.accountsservice.exceptions.DatabaseException;
import com.mgl.accountsservice.mappers.SubAccountsEntityMapper;
import com.mgl.accountsservice.models.SubAccount;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component for deleting a single {@link SubAccount}.
 */
@Log4j2
@Component
public class DeleteSubAccountComponent {

    private final SubAccountsDao subAccountsDao;
    private final SubAccountsEntityMapper subAccountsEntityMapper;

    /**
     * .
     *
     * @param subAccountsDao .
     *
     * @param subAccountsEntityMapper .
     */
    @Autowired
    public DeleteSubAccountComponent(SubAccountsDao subAccountsDao,
                                     SubAccountsEntityMapper subAccountsEntityMapper) {
        this.subAccountsDao = subAccountsDao;
        this.subAccountsEntityMapper = subAccountsEntityMapper;
    }


    /**
     * .
     *
     * @param subAccountId .
     *
     * @return .
     */
    public Optional<SubAccount> deleteSubAccount(String subAccountId) {
        Optional<SubAccount> foundSubAccount = getSubAccount(subAccountId);

        if (foundSubAccount.isEmpty()) {
            return foundSubAccount;
        }

        subAccountsDao.deleteSubAccount(subAccountId);

        return foundSubAccount;
    }

    private Optional<SubAccount> getSubAccount(String subAccountId) {
        log.info("Attempting to fetch SubAccount with Id: {}", subAccountId);
        try {
            SubAccountEntity subAccountEntity = subAccountsDao.getSubAccount(subAccountId);
            SubAccount foundSubAccount = subAccountsEntityMapper.fromEntity(subAccountEntity);
            return Optional.of(foundSubAccount);
        } catch (DatabaseException e) {
            log.info("Exception found when trying to fetch SubAccount. ", e);
            return Optional.empty();
        }
    }

}
