package com.media.service;

import java.util.List;
import com.media.DAO.AccountDAO;
import com.media.model.Account;

public class AccountService {
    private final AccountDAO accountDAO;

    // Constructor
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // User Registration
    public Account registerAccount(Account account) {
        // Validate input conditions
        if (account.getUsername() == null || account.getUsername().trim().isEmpty() || account.getPassword() == null || account.getPassword().length() < 4) {
            return null;  // Invalid input
        }

        // Check if the username already exists
        if (accountDAO.doesUsernameExist(account.getUsername())) {
            return null;  // Username is taken
        }

        // Create new account
        return accountDAO.createAccount(account);
    }

    // User Login
    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;  // Return the logged-in account
        }
        return null;  // Login failed
    }

    // Retrieve all accounts
    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }
}
