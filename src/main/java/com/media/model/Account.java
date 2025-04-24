package com.media.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    
    @JsonProperty("account_id")
    private int account_id;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;

    // Default constructor
    public Account() {}

    // Constructor for creating an account with username and password
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor with account_id
    public Account(int account_id, String username, String password) {
        this.account_id = account_id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getAccount_id() { 
        return account_id; 
    }

    public void setAccount_id(int account_id) { 
        this.account_id = account_id; 
    }

    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    // Overridden equals method to compare Account objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return account_id == account.account_id && username.equals(account.username) && password.equals(account.password);
    }

    // Overridden toString method to represent Account object as a string
    @Override
    public String toString() {
        return "Account{" + "account_id=" + account_id + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }
}
