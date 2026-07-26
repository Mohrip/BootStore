package com.io.CoreBackend.customer.service;

import com.io.CoreBackend.customer.dto.*;
import com.io.CoreBackend.customer.entity.Customer;
import com.io.CoreBackend.customer.mapper.CustomerMapper;
import com.io.CoreBackend.customer.repository.CustomerRepository;
import com.io.CoreBackend.shared.exception.BusinessRuleException;
import com.io.CoreBackend.shared.exception.ResourceNotFoundException;
import org.springframework.security.core.AuthenticationException;
import com.io.CoreBackend.shared.security.JwtService;
import com.io.CoreBackend.shared.security.TokenDenylistService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenDenylistService denylistService;

    public AuthResponse register(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Email already registered");
        }
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Username already taken");
        }
        if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessRuleException("Phone number already registered");
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer = customerRepository.save(customer);

        log.info("Registered customer id={}", customer.getId());
        return buildAuthResponse(customer);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException | LockedException ex) {
            throw new BusinessRuleException("Account is not active");
        } catch (AuthenticationException ex) {
            throw new BusinessRuleException("Invalid email or password");
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Invalid email or password"));

        customer.setLastLoginAt(LocalDateTime.now());
        customerRepository.save(customer);

        return buildAuthResponse(customer);
    }

    public void logout(String token) {
        Claims claims = jwtService.parse(token);
        denylistService.revoke(claims.getId(), claims.getExpiration());
        log.info("Revoked token jti={}", claims.getId());
    }

    public void deleteAccount(String email, String token) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", email));

        customer.setEnabled(false);
        customerRepository.save(customer);

        Claims claims = jwtService.parse(token);
        denylistService.revoke(claims.getId(), claims.getExpiration());

        log.info("Deactivated account id={}", customer.getId());
    }

    private AuthResponse buildAuthResponse(Customer customer) {
        return AuthResponse.builder()
                .token(jwtService.generateToken(customer.getEmail()))
                .tokenType("Bearer")
                .expiresInMs(jwtService.getExpirationMs())
                .customer(customerMapper.toResponse(customer))
                .build();
    }
}