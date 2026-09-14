package com.io.CoreBackend.shared.Idempotency;

import com.io.CoreBackend.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "idempotency_keys",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_idempotency_key_customer",
                columnNames = {"idempotency_key", "customer_id"}),
        indexes = @Index(name = "idx_idempotency_expires_at", columnList = "expires_at"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class IdempotencyRecord extends BaseEntity {

    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String idempotencyKey;






}
