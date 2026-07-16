package com.io.CoreBackend.customer.mapper;

import com.io.CoreBackend.customer.dto.*;
import com.io.CoreBackend.customer.entity.Customer;
import org.springframework.stereotype.Component;



@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }
}
