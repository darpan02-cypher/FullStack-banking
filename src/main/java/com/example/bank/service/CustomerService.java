package com.example.bank.service;

import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.model.AccountDetails;
import com.example.bank.model.CustomerDetails;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SecureRandom secureRandom = new SecureRandom();

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
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        CustomerDetails existingCustomer = getCustomerById(customerId);
        customerRepository.delete(existingCustomer);
    }

    private String generateAccNo() {
        return String.valueOf(1_000_000_000L + secureRandom.nextInt(900_000_000));
    }
}
