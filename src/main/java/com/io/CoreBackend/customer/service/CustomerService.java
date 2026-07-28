package com.io.CoreBackend.customer.service;

import com.io.CoreBackend.customer.dto.CustomerResponse;
import com.io.CoreBackend.customer.entity.Customer;
import com.io.CoreBackend.customer.mapper.CustomerMapper;
import com.io.CoreBackend.customer.repository.CustomerRepository;
import com.io.CoreBackend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return customerMapper.toResponse(customer);
    }

    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", email));
        return customerMapper.toResponse(customer);
    }

    /** Module API: managed entity for other modules that need a FK reference. */
    public Customer findEntityByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", email));
    }
}
