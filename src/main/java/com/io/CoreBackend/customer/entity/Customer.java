package com.io.CoreBackend.customer.entity;

import com.io.CoreBackend.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer  extends BaseEntity {


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false,  unique = true)
    private String email;

    @Column(nullable = false,  unique = true)
    private String phone;


    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;


    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private boolean isEnabled = true;

    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    private boolean isAccountLocked = false;


    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.CUSTOMER;

}