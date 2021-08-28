package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Defines the response for retrieving all the Accounts.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetAccountsResponse extends BaseResponse {

    private List<Account> accounts;

    /**
     * Default constructor.
     *
     * @param message .
     *
     * @param accounts .
     */
    @Builder
    public GetAccountsResponse(String message,
                               List<Account> accounts) {
        super(message);
        this.accounts = accounts;
    }
}
