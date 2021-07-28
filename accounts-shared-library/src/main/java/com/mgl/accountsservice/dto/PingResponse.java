package com.mgl.accountsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the DTO for the "ping" Operation.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PingResponse {
    private boolean healthy;
}
