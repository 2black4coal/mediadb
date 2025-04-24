package com.media.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.media.model.Message;
import com.project.DeviceManager;

public class MessageDAO {

    // API #3: Post a new message
    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection connection = DeviceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) return null;

            try (ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys()) {
                if (pkeyResultSet.next()) {
                    // Retrieve the generated message ID from the ResultSet
                    // The getInt(1) method retrieves the first column of the ResultSet, which is the generated message ID
                    // The generated message ID is the primary key assigned by the database for the newly inserted message
                    // The message ID is typically an auto-incremented value, meaning it automatically increments for each new message
                    // The generated message ID is used to uniquely identify the message in the database

                    int generated_message_id = pkeyResultSet.getInt(1);
                    // Create a new Message object with the generated ID and other details
                    // The new Message object is created with the generated message ID, user ID, message text, and timestamp
                    // The new Message object represents the message that was just created in the database
                    // The new Message object is returned to the caller, allowing them to access the details of the created message
                    // The new Message object contains the same details as the original message, but with the generated ID
                    // The new Message object is returned to the caller, allowing them to access the details of the created message
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating message: " + e.getMessage());
        }
        return null;
    }

    

    // Get all messages
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        try (Connection connection = DeviceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving messages: " + e.getMessage());
        }
        return messages;
    }



// Get a message by ID
public Message getMessageById(int message_id) {
    String sql = "SELECT * FROM message WHERE message_id = ?";
    try (Connection connection = DeviceManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, message_id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return new Message(
                rs.getInt("message_id"),
              
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}







    // Delete message by ID
    public Message deleteMessageById(int message_id) {
        // First, retrieve the message to be deleted
        Message message = getMessageById(message_id);
        if (message != null) {
            // If the message exists, proceed with deletion
            String sql = "DELETE FROM message WHERE message_id = ?";
            try (Connection connection = DeviceManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setInt(1, message_id);  // Set the message_id to delete
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Return the deleted message (from previously fetched message)
                    return message;
                }
            } catch (SQLException e) {
                System.err.println("Error deleting message: " + e.getMessage());
            }
        }
        return null;  // Return null if no message was deleted (message ID not found)
    }

    // Update message by ID
   
    public Message updateMessageById(int message_id, String newMessageText) {
        // Validate the new message text
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return null;  // Invalid message text
        }
    
        // First, check if the message exists
        Message existingMessage = getMessageById(message_id);
        if (existingMessage == null) {
            return null;  // Message does not exist
        }
    
        // Update the message if it exists
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try (Connection connection = DeviceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
    
            preparedStatement.setString(1, newMessageText);  // Update the message text
            preparedStatement.setInt(2, message_id);  // Identify which message to update
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // If the update is successful, return the updated message
                return getMessageById(message_id);  // Retrieve and return the updated message
            }
        } catch (SQLException e) {
            System.err.println("Error updating message: " + e.getMessage());
        }
        return null;  // Return null if update failed

    }


   
    // Retrieve all messages written by a particular user
    public List<Message> getMessagesByAccountId(int account_id) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";  // Query to get messages for a specific account_id

        try (Connection connection = DeviceManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, account_id);  // Setting the account_id as the filter
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                );
                messages.add(message);  // Adding the message to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;  // If no messages are found, it returns an empty list
    }
}
