package com.media.service;

import java.util.List;

import com.media.DAO.AccountDAO;  // For interacting with AccountDAO
import com.media.DAO.MessageDAO;  // For interacting with MessageDAO
import com.media.model.Message;

public class MessageService {
     // Assuming you have an AccountDAO class for user validation

    private final MessageDAO messageDAO;
    private final AccountDAO accountDAO;

    // Constructor to initialize messageDAO and accountDAO
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {  
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO; // Initialize the accountDAO
    }

    // API #3: Post a new message
    public Message postMessage(Message message) {

        // Validation: Check if the message is not blank and has a valid length

        // the message.getMessage_text() method retrieves the message text from the Message object
        // The trim() method removes any leading or trailing whitespace from the message text
        // The isEmpty() method checks if the trimmed message text is empty
        // The length() method checks if the message text exceeds the maximum allowed length (255 characters in this case)
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null;  // Message cannot be blank or too long
        }

        // Check if the user ID is valid (assuming you have a method to validate it)
        // The message.getPosted_by() method retrieves the user ID from the Message object
        // The posted_by field should be a positive integer representing the user ID
        if (message.getPosted_by() <= 0) {
            return null;  // Invalid user ID
        }

        // Ensure that the user exists in the account table
        // The accountDAO.doesAccountExist(message.getPosted_by()) method checks if the user ID exists in the database

        if (!accountDAO.doesAccountExist(message.getPosted_by())) {  // message.getPosted_by() retrieves the user ID from the Message object
            return null;  // User does not exist
            
        }

        // If all conditions are met, save the message
        return messageDAO.createMessage(message);
    }






     // Get all messages
     public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

   



    public Message getMessageById(int message_id) {
        // Retrieve the message by ID from the database
        return messageDAO.getMessageById(message_id);
    }



     // Delete a message by ID
     public Message deleteMessageById(int message_id) {
        // Call the DAO to delete the message and return the deleted message (if exists)
        return messageDAO.deleteMessageById(message_id);
    }







    // Update message by ID
    // It will update the message with the given ID and return the updated message if it exists
    // 
    public Message updateMessageById(int message_id, String newMessageText) {
        // Call DAO to update the message and return the updated message (if successful)
        return messageDAO.updateMessageById(message_id, newMessageText);
    }
 




// Retrieve all messages written by a particular user
public List<Message> getMessagesByAccountId(int account_id) {
    return messageDAO.getMessagesByAccountId(account_id);  // Calls DAO method to get messages by account_id
}















}
