package com.example.bank.service;

import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.model.AccountDetails;
import com.example.bank.model.CustomerDetails;
import com.example.bank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public List<CustomerDetails> getAllCustomers() {
        return customerRepository.findAll();
    }

    public CustomerDetails createCustomer(CustomerDetails customer) {
        if (customer.getAccountDetails() != null) {
            for (AccountDetails account : customer.getAccountDetails()) {
                account.setCustomer(customer);
                if (account.getAccNo() == null) {
                    account.setAccNo(generateAccNo());
                }
            }
        }
        return customerRepository.save(customer);
    }

    public CustomerDetails getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public CustomerDetails updateCustomer(Long customerId, CustomerDetails updatedCustomer) {
        CustomerDetails existingCustomer = getCustomerById(customerId);
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setAge(updatedCustomer.getAge());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        if (updatedCustomer.getAccountDetails() != null) {
            existingCustomer.getAccountDetails().removeIf(existingAccount ->
                    updatedCustomer.getAccountDetails().stream()
                            .noneMatch(updatedAccount -> updatedAccount.getAccNo() != null
                                    && updatedAccount.getAccNo().equals(existingAccount.getAccNo())));

            for (AccountDetails updatedAccount : updatedCustomer.getAccountDetails()) {
                AccountDetails existingAccount = existingCustomer.getAccountDetails().stream()
                        .filter(account -> updatedAccount.getAccNo() != null
                                && updatedAccount.getAccNo().equals(account.getAccNo()))
                        .findFirst()
                        .orElse(null);

                if (existingAccount == null) {
                    updatedAccount.setCustomer(existingCustomer);
                    if (updatedAccount.getAccNo() == null) {
                        updatedAccount.setAccNo(generateAccNo());
                    }
                    existingCustomer.getAccountDetails().add(updatedAccount);
                } else {
                    existingAccount.setAccPin(updatedAccount.getAccPin());
                    existingAccount.setAccountType(updatedAccount.getAccountType());
                }
            }
        }

        return customerRepository.save(existingCustomer);
    }

    @Transactional
    public void deleteCustomer(Long customerId) {
        CustomerDetails existingCustomer = getCustomerById(customerId);
        existingCustomer.getAccountDetails().clear();
        customerRepository.delete(existingCustomer);
        customerRepository.flush();
    }

    private String generateAccNo() {
        return String.valueOf(1_000_000_000L + secureRandom.nextInt(900_000_000));
    }
}
