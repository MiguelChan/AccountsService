package com.mgl.accountsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Consistent error envelope; HTTP status determines the failure category. */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private String code;
    private String message;
}
