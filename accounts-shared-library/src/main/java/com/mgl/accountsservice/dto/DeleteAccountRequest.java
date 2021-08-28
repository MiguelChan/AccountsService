package com.mgl.accountsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines a Request for Deleting a {@link com.mgl.accountsservice.models.Account}.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteAccountRequest {

    private String accountId;

}
