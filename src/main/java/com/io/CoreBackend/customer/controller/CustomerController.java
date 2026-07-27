package com.io.CoreBackend.customer.controller;

import com.io.CoreBackend.customer.dto.CustomerResponse;
import com.io.CoreBackend.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /** The caller's own profile, resolved from the authenticated principal. */
    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getCurrentCustomer(Authentication authentication) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(authentication.getName()));
    }

    /** Admin-only: looking up arbitrary customers by id would otherwise be an IDOR. */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
}
