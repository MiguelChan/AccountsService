package com.mgl.accountsservice.controllers;

import com.mgl.accountsservice.components.DeleteSubAccountComponent;
import com.mgl.accountsservice.dto.DeleteSubAccountResponse;
import com.mgl.accountsservice.exceptions.ResourceNotFoundException;
import com.mgl.accountsservice.models.SubAccount;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Exposes child deletion with explicit not-found semantics. */
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubAccountsController {

    private final DeleteSubAccountComponent deleteSubAccountComponent;

    public SubAccountsController(DeleteSubAccountComponent deleteSubAccountComponent) {
        this.deleteSubAccountComponent = deleteSubAccountComponent;
    }

    /** Deletes a child or reports HTTP 404. */
    @DeleteMapping("/subAccounts/{subAccountId}")
    public DeleteSubAccountResponse deleteSubAccount(@PathVariable String subAccountId) {
        SubAccount child = deleteSubAccountComponent.deleteSubAccount(subAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("SubAccount not found"));
        return DeleteSubAccountResponse.builder().success(true).deletedSubAccount(child).build();
    }
}
