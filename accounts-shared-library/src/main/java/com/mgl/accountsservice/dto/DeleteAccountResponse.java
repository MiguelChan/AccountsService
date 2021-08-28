package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response used for when an Account gets deleted.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeleteAccountResponse extends BaseResponse {

    private boolean success;
    private Account deletedAccount;

    /**
     * .
     *
     * @param success .
     *
     * @param message .
     *
     * @param deletedAccount .
     */
    @Builder
    public DeleteAccountResponse(boolean success, String message, Account deletedAccount) {
        super(message);
        this.success = success;
        this.deletedAccount = deletedAccount;
    }

}
