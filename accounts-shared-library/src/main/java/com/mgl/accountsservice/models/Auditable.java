package com.mgl.accountsservice.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines some basic properties that can be used for Auditing an Object.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Auditable {
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
