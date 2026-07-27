package com.io.CoreBackend.customer.controller;

import com.io.CoreBackend.customer.dto.*;
import com.io.CoreBackend.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/createUser")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customerRequest));

    }






}
