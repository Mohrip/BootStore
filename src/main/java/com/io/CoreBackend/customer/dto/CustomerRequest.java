package com.io.CoreBackend.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerRequest {

    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message = "Phone is required")
    //@Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "Invalid phone format")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    private String address;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 60, message = "Password must be at least 8 characters")
    private String password;

}
