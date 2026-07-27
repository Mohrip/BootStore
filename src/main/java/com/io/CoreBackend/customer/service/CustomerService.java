package com.io.CoreBackend.customer.service;

import com.io.CoreBackend.customer.dto.*;
import com.io.CoreBackend.customer.entity.Customer;
import com.io.CoreBackend.customer.mapper.CustomerMapper;
import com.io.CoreBackend.customer.repository.CustomerRepository;
import com.io.CoreBackend.shared.exception.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;



    public CustomerResponse registerCustomer(CustomerRequest customerRequest) {
        log.info("Registering new customer with email: {}", customerRequest.getEmail());
        if(customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new BusinessRuleException("Email already exists: " + customerRequest.getEmail());
        }
        Customer customer = customerMapper.toEntity(customerRequest);
        customer = customerRepository.save(customer);
        log.info("New customer with email: {}", customer.getEmail());
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return customerMapper.toResponse(customer);
    }



}
