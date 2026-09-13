package com.example.bank.repository;

import com.example.bank.model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerDetails, Long> {
}
