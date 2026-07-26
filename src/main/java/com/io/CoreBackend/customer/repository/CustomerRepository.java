package com.io.CoreBackend.customer.repository;


import com.io.CoreBackend.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;



@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {



    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Customer> findByEmail(String email);

    boolean existByUsername(String username);

}
