package com.mgl.accountsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base Response that includes a message in case something goes wrong.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponse {
    private String message;
}
