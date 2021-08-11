package com.mgl.accountsservice.dao.entities;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the {@link com.mgl.accountsservice.models.Account}
 * that can be inserted into a Database.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountEntity {
    private String id;
    private String name;
    private String accountType;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
