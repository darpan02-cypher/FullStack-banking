package com.example.bank.service;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.model.AccountDetails;
import com.example.bank.model.CustomerDetails;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AccountDetails createAccountForCustomer(Long customerId, AccountDetails account) {
        CustomerDetails customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        account.setCustomer(customer);
        if (account.getAccNo() == null) {
            account.setAccNo(generateAccNo());
        }
        return accountRepository.save(account);
    }

    public List<AccountDetails> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountDetails getAccountByAccNo(String accNo) {
        return accountRepository.findByAccNo(accNo)
                .orElseThrow(() -> new AccountNotFoundException(accNo));
    }

    public AccountDetails updateAccount(String accNo, AccountDetails updatedAccount) {
        AccountDetails existingAccount = getAccountByAccNo(accNo);
        existingAccount.setAccPin(updatedAccount.getAccPin());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(String accNo) {
        AccountDetails existingAccount = getAccountByAccNo(accNo);
        accountRepository.delete(existingAccount);
    }

    private String generateAccNo() {
        return String.valueOf(1_000_000_000L + secureRandom.nextInt(900_000_000));
    }
}
