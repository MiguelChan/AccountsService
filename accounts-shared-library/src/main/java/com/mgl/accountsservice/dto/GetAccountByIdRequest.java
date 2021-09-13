package com.mgl.accountsservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * The DTO for fetching an Account by It's Id.
 */
@Data
@Builder
public class GetAccountByIdRequest {

    private String accountId;

}
