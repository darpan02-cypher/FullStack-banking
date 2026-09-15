package com.example.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.model.AccountDetails;
import com.example.bank.service.AccountService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/createAccountForCustomer/{customerId}")
    public ResponseEntity<AccountDetails> createAccountForCustomer(@PathVariable Long customerId,
            @RequestBody AccountDetails account) {
        AccountDetails created = accountService.createAccountForCustomer(customerId, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<AccountDetails>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/getAccountByAccNo/{accNo}")
    public ResponseEntity<AccountDetails> getAccountByAccNo(@PathVariable String accNo) {
        return ResponseEntity.ok(accountService.getAccountByAccNo(accNo));
    }

    @PutMapping("/updateAccount/{accNo}")
    public ResponseEntity<AccountDetails> updateAccount(@PathVariable String accNo,
            @RequestBody AccountDetails account) {
        return ResponseEntity.ok(accountService.updateAccount(accNo, account));
    }

    @DeleteMapping("/deleteAccount/{accNo}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accNo) {
        accountService.deleteAccount(accNo);
        return ResponseEntity.noContent().build();
    }
}
