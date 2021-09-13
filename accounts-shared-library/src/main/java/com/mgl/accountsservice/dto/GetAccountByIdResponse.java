package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Defines the response for retrieving a single Account.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAccountByIdResponse extends BaseResponse {

    private Account account;
    private boolean success;

    /**
     * Default constructor.
     *
     * @param message .
     *
     * @param account .
     *
     * @param success .
     */
    @Builder
    public GetAccountByIdResponse(String message, Account account, boolean success) {
        super(message);
        this.account = account;
        this.success = success;
    }

}
