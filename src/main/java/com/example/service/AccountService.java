package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // username not blank, password >= 4, username already exist -> 200 response status
    public Account createAccount(Account account) {

        if (account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }

        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
            if (existingAccount.isPresent()) {
                return new Account();
            }

        return accountRepository.save(account);
    }

    // user and password = existing account, if not = 401
    public Account loginAccount(Account account) {
        
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());

        if(existingAccount.isPresent() && existingAccount.get().getPassword().equals(account.getPassword())) {
            return existingAccount.get();
        }
        return null;
    }

}

