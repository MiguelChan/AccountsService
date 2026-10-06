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
    private int limit;
    private int offset;
    private boolean hasMore;

    /**
     * Default constructor.
     *
     * @param message .
     *
     * @param accounts .
     */
    @Builder
    public GetAccountsResponse(String message,
                               List<Account> accounts,
                               int limit,
                               int offset,
                               boolean hasMore) {
        super(message);
        this.accounts = accounts;
        this.limit = limit;
        this.offset = offset;
        this.hasMore = hasMore;
    }
}
