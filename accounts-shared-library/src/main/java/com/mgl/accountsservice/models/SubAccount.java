package com.mgl.accountsservice.models;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Defines a SubAccount.
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class SubAccount extends Auditable {
    private String id;
    private String description;

    /**
     * .
     *
     * @param createdBy .
     *
     * @param createdAt .
     *
     * @param lastUpdatedAt .
     *
     * @param id .
     *
     * @param description .
     *
     */
    @Builder(toBuilder = true)
    public SubAccount(String createdBy,
                      LocalDateTime createdAt,
                      LocalDateTime lastUpdatedAt,
                      String id,
                      String description) {
        super(createdBy, createdAt, lastUpdatedAt);
        this.id = id;
        this.description = description;
    }
}
