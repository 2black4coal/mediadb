package com.media.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.model.Message;
import com.media.service.MessageService;

public class MessageController {
    private final MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public void registerRoutes(Javalin app) {
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
    }

    private void postMessageHandler(Context ctx) {
        try {
            // Parse the request body to create a new Message object
             // Using ObjectMapper to convert JSON string to Message object
            // The ObjectMapper will automatically map the JSON fields to the Message class fields
            // This is a standard way to parse JSON in Java using Jackson library
             // The ctx.body() method retrieves the body of the HTTP request as a string
            // The readValue method of ObjectMapper converts the JSON string into a Message object
             // The Message class should have fields that match the JSON structure
             // For example, if the JSON has "message_text" and "posted_by", the Message class should have corresponding fields
            Message message = objectMapper.readValue(ctx.body(), Message.class);
             //call the postMessage method from the messageService to save the message
            // The postMessage method should handle the logic of saving the message to the database
             // It may also include validation checks, such as ensuring the message is not blank and the user ID is valid
            // The messageService is an instance of the MessageService class, which contains the business logic for handling messages
             // The postMessage method should return the created message object, which may include an ID assigned by the database
            // The createdMessage object will contain the message that was saved to the database, including any auto-generated fields like IDs
             // The postMessage method may return null if the message could not be created due to validation errors or other issues
            // The createdMessage object will contain the message that was saved to the database, including any auto-generated fields like IDs
            Message createdMessage = messageService.postMessage(message); 
            // Check if the message was created successfully

            if (createdMessage != null) {
                // return the created message with a 201 status code
                // and the location of the new resource in the response header

                ctx.json(createdMessage).status(201);//convert the createdMessage object back to JSON format and set the HTTP status code to 201 (Created)
            } else { 
                ctx.status(400).result("Invalid message data");
            }
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages).status(200);
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message).status(200);
            } else {
                ctx.status(404).result("Message not found");
            }
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }

    private void deleteMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
    
            // Call service method to delete the message
            Message deletedMessage = messageService.deleteMessageById(messageId);
    
            if (deletedMessage != null) {
                // Return 200 OK with deleted message information
                ctx.status(200).json(deletedMessage);
            } else {
                // Return 404 if no message was found
                ctx.status(404).result("Message not found.");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid message ID format.");
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
    
    

    private void updateMessageByIdHandler(Context ctx) { 
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            
            // Extract only the new message text from the request body
            Message updatedMessage = objectMapper.readValue(ctx.body(), Message.class);
            String newMessageText = updatedMessage.getMessage_text();
    
            // Call the correct service method
            Message result = messageService.updateMessageById(messageId, newMessageText);
    
            if (result != null) {
                ctx.json(result).status(200);
            } else {
                ctx.status(400).result("Failed to update the message. Ensure the message exists and the text is valid.");
            }
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
    

    private void getMessagesByAccountIdHandler(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            ctx.json(messages).status(200);
        } catch (Exception e) {
            ctx.status(500).result("Internal Server Error");
        }
    }
}
