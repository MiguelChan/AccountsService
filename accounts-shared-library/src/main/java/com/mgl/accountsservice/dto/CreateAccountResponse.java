package com.mgl.accountsservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Response to be used when an Account Creation is requested.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAccountResponse extends BaseResponse {

    private boolean success;
    private String accountId;

    /**
     * .
     *
     * @param success .
     *
     * @param message .
     *
     * @param accountId .
     */
    @Builder
    public CreateAccountResponse(
        boolean success,
        String message,
        String accountId
    ) {
        super(message);
        this.success = success;
        this.accountId = accountId;
    }
}
