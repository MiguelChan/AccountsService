package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response used when putting an account.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutAccountResponse extends BaseResponse {

    private boolean success;
    private Account updatedAccount;

    /**
     * .
     *
     * @param message .
     *
     * @param success .
     *
     * @param updatedAccount .
     */
    @Builder
    public PutAccountResponse(String message, boolean success, Account updatedAccount) {
        super(message);
        this.success = success;
        this.updatedAccount = updatedAccount;
    }

}
