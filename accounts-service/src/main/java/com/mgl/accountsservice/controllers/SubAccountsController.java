package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.components.DeleteSubAccountComponent;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.models.SubAccount;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The SubAccounts REST Controller.
 */
@Log4j2
@RestController
@RequestMapping(
    value = "/",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class SubAccountsController {

    private final DeleteSubAccountComponent deleteSubAccountComponent;

    @Autowired
    public SubAccountsController(DeleteSubAccountComponent deleteSubAccountComponent) {
        this.deleteSubAccountComponent = deleteSubAccountComponent;
    }

    /**
     * Deletes a {@link SubAccount} .
     *
     * @param subAccountId .
     *
     * @return .
     */
    @DeleteMapping(value = "/subAccounts/{subAccountId}", consumes = MediaType.ALL_VALUE)
    public DeleteSubAccountResponse deleteSubAccount(@PathVariable String subAccountId) {
        log.info("Attempting to Delete SubAccount with Id: {}", subAccountId);
        try {
            Optional<SubAccount> deletedSubAccount =
                deleteSubAccountComponent.deleteSubAccount(subAccountId);

            if (deletedSubAccount.isEmpty()) {
                return DeleteSubAccountResponse.builder()
                    .message("SubAccount not found")
                    .success(false)
                    .build();
            }

            return DeleteSubAccountResponse.builder()
                .success(true)
                .deletedSubAccount(deletedSubAccount.get())
                .build();
        } catch (Exception e) {
            log.error("An error occurred when trying to delete the SubAccount", e);
            return DeleteSubAccountResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        }
    }

}
