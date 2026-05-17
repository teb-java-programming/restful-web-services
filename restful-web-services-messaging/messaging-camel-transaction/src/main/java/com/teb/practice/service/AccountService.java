package com.teb.practice.service;

import com.teb.practice.AccountRepository;
import com.teb.practice.exception.TransactionException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public double deposit(double amount) {

        return accountRepository.updateBalance(accountRepository.getBalance() + amount);
    }

    public double withdraw(double amount) {

        if (accountRepository.getBalance() < amount)
            throw new TransactionException("Insufficient balance");

        return accountRepository.updateBalance(accountRepository.getBalance() - amount);
    }
}
