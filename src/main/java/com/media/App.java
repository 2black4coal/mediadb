package com.media;

import io.javalin.Javalin;
import com.media.DAO.AccountDAO;
import com.media.DAO.MessageDAO;
import com.media.controller.AccountController;
import com.media.controller.MessageController;
import com.media.service.AccountService;
import com.media.service.MessageService;

public class App {
    public static void main(String[] args) {
        // Initialize DAO and Service for Account
        AccountDAO accountDAO = new AccountDAO();
        AccountService accountService = new AccountService(accountDAO);
        AccountController accountController = new AccountController(accountService);

        // Initialize DAO and Service for Message
        MessageDAO messageDAO = new MessageDAO();
        MessageService messageService = new MessageService(messageDAO, accountDAO);
        MessageController messageController = new MessageController(messageService);

        // Create Javalin app and start server on port 9090
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost()); // Allow all hosts for CORS
            });
        }).start(9090);

        // Register routes through the controllers
        accountController.registerRoutes(app); // Account routes
        messageController.registerRoutes(app); // Message routes

        // Base route for testing
        app.get("/", ctx -> ctx.result("Hello, Javalin!"));
    }
}
