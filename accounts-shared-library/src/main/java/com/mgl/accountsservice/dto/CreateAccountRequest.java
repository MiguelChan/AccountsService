package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines a Request for creating a {@link Account}.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountRequest {
    private String requestingUser;
    private Account account;
}
