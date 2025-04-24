package com.media.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.model.Account;
import com.media.service.AccountService;

import java.util.List;

public class AccountController {
    private final AccountService accountService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON Parser

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/accounts/register", this::handleRegistration);
        app.post("/accounts/login", this::handleLogin);
        app.get("/accounts", this::handleGetAllAccounts);
    }

    private void handleRegistration(Context ctx) {
        try {
            Account requestAccount = objectMapper.readValue(ctx.body(), Account.class);

            if (requestAccount.getUsername() == null || requestAccount.getUsername().trim().isEmpty()) {
                ctx.status(400).result("Invalid input: Username cannot be blank.");
                return;
            }

            if (requestAccount.getPassword() == null || requestAccount.getPassword().length() < 4) {
                ctx.status(400).result("Invalid input: Password must be at least 4 characters.");
                return;
            }

            Account createdAccount = accountService.registerAccount(requestAccount);
            if (createdAccount != null) {
                ctx.status(201).json(createdAccount);
            } else {
                ctx.status(409).result("Registration failed: Username already exists.");
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).result("Invalid data provided.");
        } catch (Exception e) {
            ctx.status(500).result("Server error during registration.");
        }
    }

    private void handleLogin(Context ctx) {
        try {
            Account loginRequest = objectMapper.readValue(ctx.body(), Account.class);

            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                ctx.status(400).result("Invalid input: Username and password cannot be blank.");
                return;
            }

            Account authenticatedAccount = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());

            if (authenticatedAccount != null) {
                ctx.status(200).result("Login successful.");
            } else {
                ctx.status(401).result("Authentication failed: Invalid credentials.");
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error during login.");
        }
    }

    private void handleGetAllAccounts(Context ctx) {
        try {
            List<Account> accounts = accountService.getAllAccounts();

            if (accounts.isEmpty()) {
                ctx.status(404).result("No accounts available.");
            } else {
                ctx.status(200).json(accounts);
            }
        } catch (Exception e) {
            ctx.status(500).result("Server error while retrieving accounts.");
        }
    }
}