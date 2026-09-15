package com.example.bank.repository;

import java.util.Optional;

import com.example.bank.model.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountDetails, Long> {

    Optional<AccountDetails> findByAccNo(String accNo);
}
