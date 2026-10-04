package com.mgl.accountsservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Defines a request for retrieving all the Accounts.
 */
@Data
@Builder
public class GetAccountsRequest {
    private Integer limit;
    private Integer offset;
}
