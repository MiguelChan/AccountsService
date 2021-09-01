package com.mgl.accountsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Request for deleting a SubAccount.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteSubAccountRequest {

    private String subAccountId;

}
