package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used when trying to put (edit) an account.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PutAccountRequest {

    private Account updatedAccount;
    private String updatingUser;

}
