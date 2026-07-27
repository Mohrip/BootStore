package com.io.CoreBackend.customer.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String tokenType;
    private Long expiresInMs;
    private CustomerResponse customer;

}
