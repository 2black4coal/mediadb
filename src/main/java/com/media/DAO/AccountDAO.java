

package com.media.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.media.model.Account;
import com.project.DeviceManager;

public class AccountDAO {

    // Create a new account
   // Create a new account (Register User)
   public Account createAccount(Account account) {
    // Validate input: Username must not be blank, password must be at least 4 characters
    if (account.getUsername() == null || account.getUsername().trim().isEmpty() ||
        account.getPassword() == null || account.getPassword().length() < 4) {
        return null;  // Invalid input, return null
    }

    // Check if an account with the given username already exists
    if (doesAccountExist(account.getAccount_id())) {
        return null;  // Username already taken, return null
    }

    // Insert new account into the database
    String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.executeUpdate();

        // Retrieve the generated account_id
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            account.setAccount_id(generatedKeys.getInt(1)); // Set generated ID
            return account;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error creating account: " + e.getMessage());
    }
    return null; // Return null if registration fails
}

// Check if an account with the given username already exists
public boolean doesAccountExist(int account_id) {
    String sql = "SELECT 1 FROM account WHERE account_id = ?";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, account_id);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();  // If a record exists, return true
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


public boolean doesUsernameExist(String username) {
    String sql = "SELECT COUNT(*) FROM account WHERE username = ?";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;  // If count > 0, the username exists
        }
    } catch (SQLException e) {
        System.err.println("Error checking if username exists: " + e.getMessage());
    }
    return false;  // If no result, username doesn't exist
}

// Retrieve an account for login
public Account getAccountByUsernameAndPassword(String username, String password) {
    String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if login fails
}

// Retrieve an account by username
public Account getAccountByUsername(String username) {
    String sql = "SELECT * FROM account WHERE username = ?";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public List<Account> getAllAccounts() {
    List<Account> accounts = new ArrayList<>();
    String sql = "SELECT * FROM Account";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            accounts.add(new Account(rs.getInt("account_id"),
                                     rs.getString("username"),
                                     rs.getString("password")));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return accounts;
}

}

