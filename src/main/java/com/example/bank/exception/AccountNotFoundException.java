package com.example.bank.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String accNo) {
        super("Account not found with accNo: " + accNo);
    }
}
