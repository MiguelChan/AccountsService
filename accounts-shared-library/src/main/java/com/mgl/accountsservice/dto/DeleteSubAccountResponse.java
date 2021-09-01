package com.mgl.accountsservice.dto;

import com.mgl.accountsservice.models.SubAccount;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO to be used when a DeleteSubAccount is requested.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeleteSubAccountResponse extends BaseResponse {

    private SubAccount deletedSubAccount;
    private boolean success;

    /**
     * .
     *
     * @param success .
     *
     * @param message .
     *
     * @param deletedSubAccount .
     */
    @Builder
    public DeleteSubAccountResponse(boolean success, String message, SubAccount deletedSubAccount) {
        super(message);
        this.success = success;
        this.deletedSubAccount = deletedSubAccount;
    }

}
