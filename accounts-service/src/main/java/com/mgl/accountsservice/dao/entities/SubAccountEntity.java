package com.mgl.accountsservice.dao.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines a {@link com.mgl.accountsservice.models.SubAccount} that can be
 * inserted into a Database.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubAccountEntity {
    private String id;
    private String accountId;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
