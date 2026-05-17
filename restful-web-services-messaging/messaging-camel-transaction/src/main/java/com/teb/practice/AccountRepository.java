package com.teb.practice;

import lombok.Getter;

import org.springframework.stereotype.Component;

@Component
public class AccountRepository {

    @Getter private double balance = 0.0;

    public double updateBalance(double updatedBalance) {

        this.balance = updatedBalance;

        return this.balance;
    }
}
